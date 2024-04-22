from openie import StanfordOpenIE
import json
import nltk
from nltk.stem import WordNetLemmatizer

# https://stanfordnlp.github.io/CoreNLP/openie.html#api
# Default value of openie.affinity_probability_cap was 1/3.
properties = {
    'openie.affinity_probability_cap': 2 / 3,
}

lemmatizer = WordNetLemmatizer()

with StanfordOpenIE(properties=properties) as client:
    text = 'Barack Obama was born in Hawaii. Richard Manning wrote this sentence.'
    print('Text: %s.' % text)
    for triple in client.annotate(text):
        print('|-', triple)

    # graph_image = 'graph.png'
    # client.generate_graphviz_graph(text, graph_image)
    # print('Graph generated: %s.' % graph_image)

    # with open('pg6130.txt', encoding='utf8') as r:
    #     corpus = r.read().replace('\n', ' ').replace('\r', '')
    with open('D:\\researchPro\\testStanfordNLP\\tmpArticle.txt', encoding='utf8') as r:
        corpus = r.read().replace('\n', ' ').replace('\r', '')

    triples_corpus = client.annotate(corpus[0:5000])
    print('Corpus: %s [...].' % corpus[0:80])
    print('Found %s triples in the corpus.' % len(triples_corpus))
    print(type(triples_corpus).__name__)

    target_triples_list = []
    for triple in triples_corpus:
        triple_item = {}
        lemma_subject = lemmatizer.lemmatize(triple['subject'])
        lemma_relation = lemmatizer.lemmatize(triple['relation'])
        lemma_object = lemmatizer.lemmatize(triple['object'])
        triple_item['subject'] = lemma_subject
        triple_item['relation'] = lemma_relation
        triple_item['object'] = lemma_object
        target_triples_list.append(triple_item)
    # for triple in triples_corpus:
    #     print('|-', triple)
    with open('D:\\researchPro\\testStanfordNLP\\triples.json', 'w') as f:
        json.dump(triples_corpus, f)
    # for triple in triples_corpus[:3]:
    #     print('|-', triple)
    #     print(type(triple).__name__)
    print('[...]')