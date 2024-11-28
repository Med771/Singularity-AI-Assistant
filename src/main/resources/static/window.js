class ChatWindow extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({ mode: 'open' });

        // Вставляем HTML-структуру в компонент
        this.shadowRoot.innerHTML = `
            <style>
                @import url('https://fonts.googleapis.com/css2?family=JetBrains+Mono:ital,wght@0,100..800;1,100..800&display=swap');

                * {
                    margin: 0;
                    padding: 0;
                    font-family: "JetBrains Mono", monospace;
                    outline: none;
                    box-sizing: border-box;
                }

                body {
                    width: 100vw;
                    height: 300vh;
                    background: #000;
                    overflow-x: hidden;
                }

                .main {
                    width: 100%;
                    height: 100%;
                    display: flex;
                    justify-content: flex-end;
                    align-items: flex-end;
                }

                .dialog-window {
                    display: flex;
                    justify-content: flex-end;
                    align-items: center;
                    width: 100%;
                    height: 100px;
                    position: fixed;
                    bottom: 0;
                    left: 0;
                    padding-right: 10px;
                }

                .dialog-window__button {
                    width: 60px;
                    height: 60px;
                    background: #fff url('/image/chatIco.png') center/cover no-repeat;
                    border-radius: 50%;
                }

                .dialog-window__chat-block {
                    width: 100vw;
                    height: 100vh;
                    position: absolute;
                    bottom: 0;
                    display: none;
                    justify-content: flex-end;
                    align-items: center;
                    padding-right: 30px;
                    padding-bottom: 20px;
                }

                .dialog-window-chat {
                    display: flex;
                    flex-direction: column;
                    justify-content: space-between;
                    align-items: center;
                    width: 40%;
                    height: 80%;
                    border-radius: 20px;
                    background: #fff;
                    border: 1px solid #000;
                }

                .dialog-window-chat__header {
                    width: 100%;
                    height: 10%;
                    border-top-right-radius: 20px;
                    border-top-left-radius: 20px;
                    background: #c8bebe;
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    align-items: flex-start;
                    padding-left: 5%;
                }

                .dialog-window-chat__header__text1 {
                    font-size: 1.2em;
                }

                .dialog-window-chat__header__text2 {
                    font-size: 1em;
                }

                .dialog-window-chat__user-input-block {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    border-bottom-right-radius: 20px;
                    border-bottom-left-radius: 20px;
                    padding: 10px;
                    width: 100%;
                }

                .dialog-window-chat__user-input {
                    width: 90%;
                    height: 70%;
                    border-radius: 20px;
                    border: 1px solid #000;
                    padding: 5px 0 5px 10px;
                    font-size: 1.4em;
                }

                .dialog-window-chat__messages {
                    width: 100%;
                    flex: 1;
                    overflow-y: auto;
                    padding: 10px;
                    display: flex;
                    flex-direction: column;
                }

                .dialog-window-chat__message {
                    background: #f0f0f0;
                    border-radius: 10px;
                    padding: 10px;
                    margin-bottom: 10px;
                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                    max-width: 70%;
                    word-wrap: break-word;
                }

                .user-message {
                    align-self: flex-end;
                    background: #dcf8c6;
                }

                .bot-message {
                    align-self: flex-start;
                    background: #e5e5ea;
                }
            </style>

            <div class="dialog-window">
                <div class="dialog-window__chat-block">
                    <div class="dialog-window-chat">
                        <header class="dialog-window-chat__header">
                            <p class="dialog-window-chat__header__text1">Some text</p>
                            <p class="dialog-window-chat__header__text2">Some text</p>
                        </header>

                        <div class="dialog-window-chat__messages"></div>

                        <div class="dialog-window-chat__user-input-block">
                            <input type="text" class="dialog-window-chat__user-input" placeholder="Type a message...">
                        </div>
                    </div>
                </div>

                <div class="dialog-window__button"></div>
            </div>
        `;
    }

    connectedCallback() {
        this._initializeEventListeners();
    }

    _initializeEventListeners() {
        const shadowRoot = this.shadowRoot;
        const chatButton = shadowRoot.querySelector('.dialog-window__button');
        const dialogChat = shadowRoot.querySelector('.dialog-window__chat-block');
        const dialogWindowChat = shadowRoot.querySelector('.dialog-window-chat');
        const userInput = shadowRoot.querySelector('.dialog-window-chat__user-input');
        const messagesContainer = shadowRoot.querySelector('.dialog-window-chat__messages');

        let cooldown = false;

        // Открытие чата при нажатии на кнопку
        chatButton.addEventListener('click', (event) => {
            event.stopPropagation();  // Останавливаем всплытие события, чтобы не срабатывал обработчик документа
            dialogChat.style.display = 'flex';
            console.log('Chat button clicked!');
        });

        // Закрытие чата при клике вне окна
        document.addEventListener('click', (event) => {
            if (!dialogWindowChat.contains(event.target) && !chatButton.contains(event.target)) {
                dialogChat.style.display = 'none';
                console.log('Chat window closed!');
            }
        });

        // Останавливаем всплытие события клика внутри окна чата
        dialogWindowChat.addEventListener('click', (event) => {
            event.stopPropagation();  // Останавливаем всплытие события, чтобы чат не закрылся
        });

        // Добавляем обработчик кликов на поле ввода, чтобы оно не закрывало окно
        userInput.addEventListener('click', (event) => {
            event.stopPropagation();  // Останавливаем всплытие события
        });

        // Обработчик нажатия клавиши "Enter"
        userInput.addEventListener('keypress', (event) => {
            if (event.key === 'Enter' && userInput.value.trim() !== '' && !cooldown) {
                this.handleUserMessage(userInput);
            }
        });
    }

    // Обработчик сообщений пользователя
    async handleUserMessage(userInput) {
        const messagesContainer = this.shadowRoot.querySelector('.dialog-window-chat__messages');
        const userMessage = document.createElement('div');
        userMessage.className = 'dialog-window-chat__message user-message';
        userMessage.textContent = userInput.value;

        // Добавляем пользовательское сообщение в чат
        messagesContainer.appendChild(userMessage);

        // Очищаем поле ввода
        userInput.value = '';

        // Прокручиваем контейнер сообщений
        messagesContainer.scrollTop = messagesContainer.scrollHeight;

        // Включаем кулдаун
        let cooldown = true;
        setTimeout(() => {
            cooldown = false;
        }, 2000); // 2 секунды кулдауна

        // Отправка сообщения на сервер
        this.sendMessageToServer(userMessage.textContent);
    }

    async sendMessageToServer(message) {
        try {
            // Пример отправки данных на сервер
            const response = await fetch('http://localhost:8080/window/query', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({query: message })
            });

            if (!response.ok) {
                throw new Error('Server response not ok');
            }

            const data = await response.json();
            this.handleBotResponse(data.answer);
        } catch (error) {
            console.error('Error sending message:', error);
            this.handleBotResponse('Ой, что-то не так');
        }
    }

    handleBotResponse(botMessageText) {
        const messagesContainer = this.shadowRoot.querySelector('.dialog-window-chat__messages');
        const botMessage = document.createElement('div');
        botMessage.className = 'dialog-window-chat__message bot-message';
        botMessage.textContent = botMessageText;

        messagesContainer.appendChild(botMessage);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    // Методы для изменения цветов
    setDialogBackgroundColor(color) {
        const chatBlock = this.shadowRoot.querySelector('.dialog-window-chat');
        chatBlock.style.backgroundColor = color;
    }

    setUserMessageColor(color) {
        const userMessages = this.shadowRoot.querySelectorAll('.user-message');
        userMessages.forEach(msg => {
            msg.style.backgroundColor = color;
        });
    }

    setBotMessageColor(color) {
        const botMessages = this.shadowRoot.querySelectorAll('.bot-message');
        botMessages.forEach(msg => {
            msg.style.backgroundColor = color;
        });
    }   

    setHeaderColor(color) {
        const header = this.shadowRoot.querySelector('.dialog-window-chat__header');
        header.style.backgroundColor = color;
    }
}

// Регистрируем компонент
customElements.define('chat-window', ChatWindow);
