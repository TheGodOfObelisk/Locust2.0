import stanfordnlp
import sys

# load Stanford NLP OpenIE model
stanfordnlp.download('en')
nlp = stanfordnlp.Pipeline(processors='openie', lang='en')

# extract triples from text
def extract_triples(text):
    doc = nlp(text)
    triples = []
    for sentence in doc.sentences:
        for triple in sentence.openie_triples:
            triples.append((triple.subject, triple.relation, triple.object))
    return triples

# get text inputted
# text = "Apple was founded by Steve Jobs, Steve Wozniak, and Ronald Wayne."
text = {sys.argv[1]}
triples = extract_triples(text)

# triples output
for triple in triples:
    print(triple[0], triple[1], triple[2])