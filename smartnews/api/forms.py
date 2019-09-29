from django import forms
from .models import News, Comment

class GetCommentForm(forms.Form):
    article_id = forms.IntegerField()
