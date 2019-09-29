from django.shortcuts import render
from .models import News, Comment
from rest_framework import generics
import requests
import json
from .forms import GetCommentForm
from bs4 import BeautifulSoup
from .serializers import NewsSerializer

def getComments(request):
    if request.method == 'POST':
        form = GetCommentForm(request.GET)
        if form.is_valid():
            article_id = form.cleaned_data['article_id']
            print(article_id)
            article = News.objects.all().filter(article_id = article_id)[0]
            comments = Comment.objects.all().filter(article = article)
            response = JsonResponse(comments, safe=False)
            return response

class NewsSendInfo(generics.ListAPIView):
    serializer_class = NewsSerializer
    queryset = News.objects.all()
