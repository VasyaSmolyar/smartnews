# -*- coding: utf-8 -*-

from parser import Api
import positiver
import checker 
import time
import json
import os
import datetime
from lxml import html

import django
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "smartnews.settings")
django.setup()
from api.models import News, Comment

token = "0484f06fd955f41221076058dfb778735cfd020c9a07a7e9bd4359564e4baf04"

while True:
    api = Api(token)
    res = api.get("https://api.vc.ru/v1.6/timeline/mainpage/popular")
    news = res["result"]

    for headline in news:
        article_id = headline["id"]
        title = headline["title"]
        if headline["cover"] == None:
            cover = ""
        else:
            cover = headline["cover"]["thumbnailUrl"]

        date = datetime.datetime.fromtimestamp(headline["date"])
        author = headline["author"]["name"]
        intro = headline["intro"]

        htmlText = html.fromstring(headline['entryContent']['html'])
        paragraphs = htmlText.xpath('//p/text()')
        text = ""

        for par in paragraphs:
            text = text + par + "\n\n"

        print(text)
        n1 = positiver.evalute(Api.cleanText(text))
        n2 = checker.evalute(Api.cleanText(text))
        if len(News.objects.all().filter(article_id = article_id)) == 0:
            article = News(article_id = article_id, title = title, intro = intro, date = date, author = author, cover = cover, article = text, authorPosition = n1, concrete = n2)
            article.save()
            time.sleep(10)
    time.sleep(400)
