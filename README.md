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

### Визуализация классов
<img width="711" height="715" alt="изображение" src="https://github.com/user-attachments/assets/b0244fdd-b42b-491a-ad3d-b0a8ae4cea88" />

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
```

## ⚙️ Используемые технологии

- **Java 21.0.7**  
- **Apache Tomcat 10** (Servlet API 6.0, совместим с Jakarta EE 10)  
- **Jakarta Servlet + JSP + JSTL** для веб-интерфейса  
- **Jackson Databind** для работы с JSON-сценами  
- **Lombok** для сокращения шаблонного кода  
- **SLF4J + Logback** для логирования  
- **JUnit 5** + **Mockito** для модульных тестов

## Лицензия

##### CC BY-NC 4.0 в следующей нотации:
  ###### RU
     Creative Commons Attribution-NonCommercial 4.0 Международная общедоступная лицензия
     
     Осуществляя Лицензионные права (определенные ниже), Вы принимаете и соглашаетесь соблюдать положения и условия настоящей публичной лицензии Creative Commons Attribution-NonCommercial 4.0 International ("Публичная лицензия"). В той мере, в какой эта Публичная лицензия может быть истолкована как договор, Вам предоставляются Лицензионные права при условии, что Вы принимаете настоящие положения и условия, а Лицензиар предоставляет Вам такие права с учетом выгод, которые Лицензиар получает от предоставления Лицензируемых материалов. на этих условиях.
    
     Вы можете:
     - Распространять — копируйте и распространяйте материал на любом носителе и в любом формате
     - Адаптировать — изменять, адаптировать и создавать на основе 
     
     На следующих условиях:
     - Авторство — вы должны предоставить ссылку на лицензию и указать, ссылку на репозиторий проекта, были ли внесены изменения. Вы можете сделать это любым разумным способом, но никоим образом не предполагающим, что лицензиар одобряет вас или ваше использование.
     - NonCommercial — Вы не можете использовать материал в коммерческих целях.
     
  ###### EN
    Creative Commons Attribution-NonCommercial 4.0 International Public License
    
    By exercising the Licensed Rights (defined below), You accept and agree to be bound by the terms and conditions of this Creative Commons Attribution-NonCommercial 4.0 International Public License ("Public License"). To the extent this Public License may be interpreted as a contract, You are granted the Licensed Rights in consideration of Your acceptance of these terms and conditions, and the Licensor grants You such rights in consideration of benefits the Licensor receives from making the Licensed Material available under these terms and conditions.
    
    You are free to:
    - Share — copy and redistribute the material in any medium or format
    - Adapt — remix, transform, and build upon the material
    
    Under the following terms:
    - Attribution — You must give appropriate credit, provide a link to the license,link to the github page project and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
    - NonCommercial — You may not use the material for commercial purposes.

## Ответственность
###### RU
    Программный продукт, представленный в этом репозитории, предоставляется "как есть" без каких-либо явных или подразумеваемых гарантий, включая, но не ограничиваясь, подразумеваемыми гарантиями коммерческой ценности, пригодности для конкретной цели и невыполнения прав. 
    Разработчик не несет ответственности за любые проблемы, ошибки или неполадки, возникшие при использовании данного продукта. Использование продукта осуществляется на ваш собственный риск.
      
###### EN
    The software product provided in this repository is provided "as is" without warranty of any kind, either express or implied, including, but not limited to, the implied warranties of merchantability, fitness for a particular purpose, and non-infringement.
    The developer is not responsible for any problems, errors or malfunctions that occur when using this product. Use of the product is at your own risk.

## Обратная связь
Ниже найдете список ссылок для связи с автором.

| Платформа     | Ссылка                                                                    | Отвечу за |
| ------------- |:-------------------------------------------------------------------------:| --------- |
| Почта         | [Ссылка](mailto:andrewoficial@yandex.ru "Ссылка")                         | 24 часа   |
| LinkedIn      | [Ссылка](https://www.linkedin.com/in/andrey-kantser-126554258/ "Ссылка")  | 3 часа    |
| Telegram      | [Ссылка](https://t.me/function_void "Ссылка")                             | 5 минут   |

