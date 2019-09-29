import requests
import json
from bs4 import BeautifulSoup

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

if __name__ == "__main__":
    token = "0484f06fd955f41221076058dfb778735cfd020c9a07a7e9bd4359564e4baf04"
    api = Api(token)
    res = api.get("https://api.vc.ru/v1.6/timeline/gaming/popular", params={'offset' : 3})
    print(Api.cleanText(res['result'][0]['entryContent']['html']))
