from newspaper import Article
from newspaper import Config
import spacy
from spacy.lang.en.stop_words import STOP_WORDS
from string import punctuation
from heapq import nlargest
import traceback

def summarize(text, per):
    try:
        nlp = spacy.load('en_core_web_sm')
        doc = nlp(text)
        tokens = [token.text for token in doc]
        word_frequencies = {}
        for word in doc:
            if word.text.lower() not in list(STOP_WORDS):
                if word.text.lower() not in punctuation:
                    if word.text not in word_frequencies.keys():
                        word_frequencies[word.text] = 1
                    else:
                        word_frequencies[word.text] += 1
        if len(word_frequencies.values()):
            max_frequency = max(word_frequencies.values())
            for word in word_frequencies.keys():
                word_frequencies[word] = word_frequencies[word] / max_frequency
            sentence_tokens = [sent for sent in doc.sents]
            sentence_scores = {}
            for sent in sentence_tokens:
                for word in sent:
                    if word.text.lower() in word_frequencies.keys():
                        if sent not in sentence_scores.keys():
                            sentence_scores[sent] = word_frequencies[word.text.lower()]
                        else:
                            sentence_scores[sent] += word_frequencies[word.text.lower()]
            select_length = int(len(sentence_tokens) * per)
            summary = nlargest(select_length, sentence_scores, key=sentence_scores.get)
            final_summary = [word.text for word in summary]
            summary = ''.join(final_summary)
        else:
            summary = "Lost Internet Connection!"
    except Exception:
        summary = "Lost Internet Connection!"
        # summary = traceback.format_exc()

    return summary


def parseWebToText(url):
       try:
           user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36"
           config = Config()
           config.browser_user_agent = user_agent
           article = Article(url,config=config)
           article.download()
           article.parse()
       except Exception as e:
            return "Lost Internet Connection!"
       return summarize(article.text, 0.5)