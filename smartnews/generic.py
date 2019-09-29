import re
import random

delims = r'[.?!]'

def getObject(src,*args):
    comments = []
    for text, target in args:
        for seq in re.split(delims, text):
            comments.append((seq.strip(), target))
    seqs = re.split(delims, src.strip())
    return (seqs, comments)

def shuffle(seqs, comments, cols = 3):
    choosen = []
    res = []
    for i in range(cols):
        while True:
            el = random.choice(seqs)
            if el in choosen:
                continue
            choosen.append(el)
            break
    for ch in choosen:
        res.append(choosen)
        res.append(random.choice(comments))
    return res

test = """Как организовать корпоративное мероприятие с участием членов семей сотрудников? В этой статье я хочу поделиться с вами моим опытом управления проектами и организации мероприятий. Сделаю акцент на событиях с участием членов семей сотрудников. После прочтения статьи у вас будут шаблоны и чек-листы, базовое понимание с чего начать организацию и чем закончить, как измерить эффективность и рассказать о результатах. Эффективность для бизнеса компании такого рода мероприятий практически невозможно привязать к финансовому результату, поэтому мы рассмотрим: зачем вообще российские компании делают такие события, как оценивать эффективность и как «упаковать» мероприятие, чтобы оно давало максимальную пользу. За свою профессиональную жизнь я много сделала сама мероприятий, много видела хороших и плохих примеров мероприятий, организованных другими. Основываясь на личном опыте и знаниях, могу выделить ключевые проблемные зоны: Отсутствие целей. Очень часто организаторы не знают какую задачу бизнеса они решают. Отсутствие внешнего и внутреннего «пиара» мероприятия. Вроде сделали и даже хорошо сделали, но никому об этом не рассказали. Полное игнорирование этапа работы над ошибками и анализа эффективности. Противостояние маркетинга, отдела персонала, ивент-отдела. В чьей зоне ответственности такие мероприятия? Корпоративное событие с участием детей или членов семей сотрудников в зоне ответственности HR-отдела и отдела маркетинговых коммуникаций. HR-отдел работает с внутренней аудиторией, а отдел маркетинга отвечает за внешние коммуникации и «упаковку» всего события. Зачем компании делают корпоративные события с участием членов семей сотрудников? Для многих компаний поддержка семейных ценностей становиться важной составляющей корпоративной культуры. Все чаще бизнес интегрирует в свою корпоративную жизнь новые форматы, укрепляющие лояльность сотрудников через нематериальную мотивацию. Так какие цели ставят перед собой компании? Приведу несколько примеров. Основной макроцелью таких мероприятий является развитие HR-бренда. Сильный бренд работодателя помогает компании привлекать квалифицированный персонал, поддерживать лояльность и вовлеченность сотрудников в работу. В рамках конкретного мероприятия обязательно нужно уточнять цели. Примерами микроцелей могут быть: Привлечение потенциальных сотрудников."""

comments = [("""
Был удивлён, что так и не увидел классические пикники. 
Все уже забыли это прекрасное решение? Нет привязки ни к чему, кроме еды и клипанит, значит могут прийти все, гигантское пространство для креатива для организаторов. 
Эх
""", 0.5),
("""
Мы у себя как раз что-то подобное устраиваем. Как минимум пару раз в год организуем корпоративные выезды на базу отдыха за город. На 3-4 дня. Там активный отдых и шашлык
""", 0.7)
]

k, v = getObject(test, *comments)
print(shuffle(k, v))
