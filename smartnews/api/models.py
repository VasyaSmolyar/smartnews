from django.db import models

class News(models.Model):
    article_id = models.IntegerField()
    title = models.CharField(max_length=250)
    intro = models.TextField()
    date = models.DateTimeField()
    author = models.CharField(max_length=250)
    cover = models.TextField(blank=True)
    article = models.TextField()
    concrete = models.FloatField(default=1)
    authorPosition = models.FloatField(default=5)

    def __str__(self):
        return self.title


class Comment(models.Model):
    comment_id = models.IntegerField()
    author = models.CharField(max_length=250)
    photo_url = models.TextField(blank=True)
    article = models.ForeignKey(News, on_delete=models.CASCADE)
    text = models.TextField()
    date = models.DateField()
    answer_id = models.IntegerField(default = -1)

    def __str__(self):
        return self.text
