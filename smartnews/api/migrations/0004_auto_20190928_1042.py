# Generated by Django 2.2.5 on 2019-09-28 10:42

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0003_auto_20190927_2132'),
    ]

    operations = [
        migrations.CreateModel(
            name='Comment',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('comment_id', models.IntegerField()),
                ('author', models.CharField(max_length=250)),
                ('photo_url', models.TextField(blank=True)),
                ('article_id', models.IntegerField()),
                ('text', models.TextField()),
                ('likes_count', models.IntegerField()),
                ('answer_id', models.IntegerField(default=-1)),
            ],
        ),
        migrations.AddField(
            model_name='news',
            name='article_id',
            field=models.IntegerField(default=1),
            preserve_default=False,
        ),
    ]
