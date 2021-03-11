from chatterbot import ChatBot
from chatterbot.conversation import Statement
from chatterbot.response_selection import get_most_frequent_response
import os

uri = 'sqlite:///data/data/com.Amira.Relax/databases/BotData.sqlite3'

Bot = ChatBot(
    'Dr. Lito',
    storage_adapter='chatterbot.storage.SQLStorageAdapter',
    database_uri=uri,
    logic_adapters=[
        {
            "import_path": "chatterbot.logic.BestMatch",
            "statement_comparison_function": "comparisons.levenshtein_distance",
            "response_selection_method": get_most_frequent_response,
            "default_response": "I am sorry, but I do not understand.",
            "maximum_similarity_threshold": 0.90
        }
    ]
)


def start_bot():
    os.chdir(os.environ["HOME"])
    return "Hi"


def start_chat(userentry):
    try:
        input_statement = Statement(text=userentry,conversation='training', persona='user:Amira')
        input_statement.add_tags('Amira')
        response = Bot.get_response(input_statement)
    except IOError:
        return str(IOError)
    return response.text
