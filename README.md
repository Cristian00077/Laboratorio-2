# Funcionalidades del chatbot
<p>
El código tienes distintas partes esenciales las cuales se mostrarán a continuación.

## Interfaz
Interfaz creada con JavaSwing. Incluye:
### TextArea
Visualización de los mensajes enviados a la API de Ollama y recibidos de esta.
### TextField
El usuario ingresa el mensaje preguntando algo que quiere que la IA (inteligencia Artificial) le responda.
### Botones
Distintas funciones como:
- **Enviar:** Ingresa el mensaje al textArea y a la API.

- **Nuevo chat:** Guarda el titulo de la conversación actual en el historial y la elimina del TextArea.

- **Historial:** Permite visualizar una lista de las conversaciones anteriores cada vez que se iniciaba un nuevo chat.

## API de Ollama
Es el enlace entre el usuario y la IA. Obtiene el mensaje del usuario, lo envía a IA y, cuando esta responde, realiza el proceso nuevamente en sentido contrario para que el usuario obtenga la respuesta final. 
Esta conexión se da realizando solicitudes por medio de la librería java.net.HttpURLConnection.

## Estructuración
En el código hay funciones distintas para preguntar, procesar y mostrar la respuesta y el historial. En el botón enviar hay un ciclo try-catch, en el cual la función de error se da si el código no es capaz de conectarse con la API de Ollama.


# ¿Cómo contrbuir al código?

## Instalar

- Primero, accede al siguiente link
	https://github.com/Cristian00077/Laboratorio-2.git

- Luego, hay que acceder al código presionando en 'Code' y luego 'Descargar en zip'
<p>

## Contribución
- Crear un Fork del repositorio
- Crear una nueva rama
- Realizar los cambios
- Enviar el pull request
