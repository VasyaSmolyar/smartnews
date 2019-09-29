import requests
import json
from bs4 import BeautifulSoup
import csv
import re

class Api:
    def __init__(self, token):
        self.token = token
    
    def get(self, method, params = {}):
        headers = {'X-Device-Token' : self.token}
        res = requests.get(method, headers=headers, params=params)
        return json.loads(res.text)

    @staticmethod
    def cleanText(html):
        cleantext = BeautifulSoup(html, "lxml").text
        return ' '.join(cleantext.split())

    def getTimeline(self):
        res = []
        sortings = ("recent", "popular", "week", "month")
        categories = ("index", "gamedev", "mainpage")
        http = "https://api.vc.ru/v1.6"
        for cat in categories:
            for srt in sortings:
                data = self.get("{}/timeline/{}/{}".format(http, cat, srt))
                for step in data['result']:
                    text = Api.cleanText(step['entryContent']['html'])
                    text = re.sub(r'{.*\:\{.*\:.*\}\}','', text)
                    res.append({'id' : step['id'], 'title' : step['title'], 'text' : text})
        return res

def getX(token):
    api = Api(token)
    with open('database/timeline.csv', 'w') as w:
        data = api.getTimeline()
        cw = csv.writer(w)
        for row in data:
            cw.writerow([row['id'], row['title'], row['text']])

if __name__ == "__main__":
    token = "0484f06fd955f41221076058dfb778735cfd020c9a07a7e9bd4359564e4baf04"
    getX(token)
