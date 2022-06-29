from chatterbot import ChatBot, response_selection
from chatterbot import comparisons
from chatterbot.conversation import Statement
from chatterbot.trainers import ChatterBotCorpusTrainer

import logging
import datetime
import nltk

logging.basicConfig(level=logging.INFO)

uri = 'sqlite:///data/data/com.relax/databases/relaxDB.db'

Bot = ChatBot(
    'Dr. Lito',
    storage_adapter='chatterbot.storage.SQLStorageAdapter',
    database_uri=uri,
    logic_adapters=[
        {
            "import_path": "chatterbot.logic.BestMatch",
            "statement_comparison_function": comparisons.levenshtein_distance,
            "response_selection_method": response_selection.get_most_frequent_response,
            "maximum_similarity_threshold": 0.75,
        }
    ]
)

# Bot.storage.drop()
# trainer = ChatterBotCorpusTrainer(Bot)
#
# trainer.train(
#     'chatterbot.corpus.english'
# )

# try:
#     nltk.data.find('tokenizers/punkt')
# except LookupError:
#     nltk.download('punkt')
#
# try:
#     nltk.data.find('tokenizers/stopwords')
# except LookupError:
#     nltk.download('stopwords')
#
# try:
#     nltk.data.find('taggers/averaged_perceptron_tagger')
# except LookupError:
#     nltk.download('averaged_perceptron_tagger')


def learn_bot_entry(user_msg, bot_msg, person="Bot", self=Bot):
    response = Statement(
        text=bot_msg,
        search_text=self.storage.tagger.get_bigram_pair_string(bot_msg),
        conversation="learn",
        created_at=datetime.datetime.now(),
        in_response_to=user_msg,
        search_in_response_to=self.storage.tagger.get_bigram_pair_string(user_msg),
        persona=person,
        tags=[]
    )
    Bot.learn_response(response)


def learn_user_entry(user_msg, bot_msg, persona, self=Bot):
    response = Statement(
        text=user_msg,
        search_text=self.storage.tagger.get_bigram_pair_string(user_msg),
        conversation="learn",
        created_at=datetime.datetime.now(),
        in_response_to=bot_msg,
        search_in_response_to=self.storage.tagger.get_bigram_pair_string(bot_msg),
        persona=persona,
        tags=[]
    )
    Bot.learn_response(response)


def get_feedback(user_msg):
    response = Bot.get_response(user_msg)
    return response
