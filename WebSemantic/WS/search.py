#!/usr/bin/python

"""
Web Semantic project Google Custom Search Engine
Limited to 100 queries per day
H4101
"""

import requests
import sys
import json

def getURL(items):
	url = []
	for item in items:
		url.append(item['link'])
	return url

def query(q, num):
	engineid = '016483631926708226135:3-ccav0kide'
	apiKey='AIzaSyDuklNeZnCe8YsQgGipgu6lUF1ZGtvWtJ4'
	urls = []

	start = num%10
	if num == 10:
		start=10

	url='https://www.googleapis.com/customsearch/v1?key=' + apiKey + '&cx=' + engineid + '&q=' + q + '&num=' + str(start)
	r = requests.get(url)
	data = json.loads(r.text)
	if 'error' in data or 'errors' in data:
		res = {'url' : url, 'error_msg' : r.text}
		return res
	urls += getURL(data['items'])

	while start < num:
		url='https://www.googleapis.com/customsearch/v1?key=' + apiKey + '&cx=' + engineid + '&q=' + q + '&start=' + str(start)
		r = requests.get(url)
		data = json.loads(r.text)
		if 'error' in data or 'errors' in data:
			res = {'url' : url, 'error_msg' : r.text}
			return res
		urls += getURL(data['items'])
		start+=10

	res = {'URLs' : urls}
	return res

if __name__ == '__main__':
	res = {}
	if len(sys.argv) < 2:
		res['error'] = 'You need to specify a query'
	else:
		num = 10
		if len(sys.argv) > 2 and int(sys.argv[2]) > 0:
			num = int(sys.argv[2])
		res = query(sys.argv[1], num)

	if len(sys.argv) == 4:
		open(sys.argv[3], 'w').write(json.dumps(res))
	else:
		print json.dumps(res)

