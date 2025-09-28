# 🕹️ Игра про прокрастинацию

Текстовая игра-симулятор рабочего дня, где нужно балансировать между временем и энергией.  
Каждый выбор влияет на то, как пройдет твой день: успеешь ли сделать задачи, хватит ли сил,  
или всё закончится внезапной «плохой концовкой».

---
## Внешний вид
<details>
<summary>📸 Посмотреть скриншоты приложения</summary>

### Страница приветствия
<img width="823" height="808" alt="изображение" src="https://github.com/user-attachments/assets/00ccf5e1-ac26-43d4-88ba-9562e8c8eabb" />

### Авторизация
<img width="520" height="607" alt="изображение" src="https://github.com/user-attachments/assets/976132bf-86f5-42c3-9c3f-ec447dbfa41d" />

### Основной геймплей
<img width="1847" height="801" alt="изображение" src="https://github.com/user-attachments/assets/903f89a8-296a-41a2-8446-9e6d0b8f6501" />

<img width="1840" height="861" alt="изображение" src="https://github.com/user-attachments/assets/3ea9e5d2-7c5a-44bd-8c9c-ee2d642f5db6" />

### Список сохранений
<img width="1848" height="529" alt="изображение" src="https://github.com/user-attachments/assets/f0dd7462-3627-45f8-8b8d-4342162997e5" />

</details>


## 🎮 Геймплей

- **Энергия** и **свободное время** — главные параметры.
- Сцены связаны между собой через JSON-файлы.
- У каждого выбора есть **эффект**:
  - `freeMinutesDelta` — изменение доступного времени.
  - `energyDelta` — изменение энергии.
  - `note` — краткий комментарий о результате.

Пример сцены:

```json
{
  "id": "start",
  "title": "Пробуждение",
  "text": "Ты просыпаешься в своей комнате. Опции дня: поспать ещё, поковыряться в телефоне или быстро встать.",
  "choices": {
    "sleep_more": {
      "id": "sleep_more",
      "description": "Поспать ещё 30 минут",
      "nextSceneId": "start",
      "effect": {
        "freeMinutesDelta": -30,
        "energyDelta": 10,
        "note": "Проспал, но немного отдохнул"
      }
    }
  }
}
