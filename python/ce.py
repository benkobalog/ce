import urllib2
import json
import sys

prefixList = ["", "k", "M", "G", "T"]

def parseQuantity(value):
    try:
        return float(value)
    except ValueError:
        index = prefixList.index(value[-1])
        num = float(value[:-1])
        return num * (1000 ** index)

quantity = parseQuantity(sys.argv[1])
from_cur = sys.argv[2].upper()
to_cur = sys.argv[3].upper()

res = urllib2.urlopen("http://api.fixer.io/latest").read()
parsed = json.loads(res)

def rate(currency):
    return float(parsed["rates"][currency])

def base():
    return parsed['base']

parsed['rates'][base()] = 1.0

change = (rate(to_cur) / rate(from_cur)) * quantity

def truncate(value, prefix = 0):
    if value / 1000 > 1:
        return truncate(value / 1000, prefix + 1)
    else:
        return "{0:.3f}".format(value) + prefixList[prefix]

#print change
print(truncate(change) + " " + to_cur)

