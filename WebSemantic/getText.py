import json
import sys
import requests
import os
import urllib
import fileinput

def main():
    apikey = '63c7975f03b0c644f62e654d26cb68763ea01b82'
    input = sys.stdin
    jsonInput = ''
    for line in input :
        jsonInput = json.loads(line)
    for line in jsonInput["URLs"]:
        url = 'http://access.alchemyapi.com/calls/url/'
        r = requests.get(url+'URLGetText?apikey='+apikey+'&url='+line.rstrip('\n')+'&outputMode=json')
        jsonInput = json.loads(r.text)
        outputText = urllib.quote_plus(jsonInput['text'].encode('utf-8'))
        print(line)
        print(outputText)

    print("#H4104 end")

if __name__ == '__main__':
    sys.stderr.write('DEBUT getText.py\n')
    main()
    sys.stderr.write('FIN getText.py\n')