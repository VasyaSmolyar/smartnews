from django.urls import path, include
from . import views
from .views import *

urlpatterns = [
    path('api/news/get', NewsSendInfo.as_view()),
    path('api/comments/get', views.getComments, name='getComments'),
    #path('', views.main, name='mainPage'),
]
