# Sentiment_Analysis--Naive-Bayes-Classifier

A Naive-Bayes Classifier that attempts to classify reviews, typed in through command-line or parsed as files, as either positive or negative.

## Authors:
Smith Gakuya, Yaqi Huang

> Files required to run program :SentAnalysis.java, SentAnalysisBest.java, SentAnalysisNeutral.java , test and train folders containing .txt files

## __KNOWN BUGS__
1. Not really a bug but noticed running the program the first time takes a longer time(we assume due to the # of files)
  
## __Texts Classifier Failed at__
1)Texts with double negatives which would be read as positive e.g.
> It was not bad

2)Texts without adjectives/adverbs i.e. words that have sentiment in context but not alone
> I would return

3)One word nouns because the nouns could have been used in a negative statement and
are thus perceived to be negative even though they have no sentiment themselves
> Pizza

4)Misspelled words
> i htd te ting

## __Texts Classifier Succeeded at__
Simple grammatical texts - 
> It was an amazing experience

## __Comparison of Base and Best Classifiers__
We attempted to use bigrams in our best classifier. In most of the files that we tested on we
only saw a minimal difference in total accuracy 0-2%. We assume perhaps the bigrams aren't frequent 
enough in most texts that we tested to have much of a difference.

To improve the system, we could pay closer attention to punctuation and capitalization since in our
system we read in the text and convert it all to lowercase for easier classification

_Best results:_
Accuracy: 74.12371872984016%
Precision(positive): 75.8%
Precision(negative): 72.43562077398936%

_Base results:_
Accuracy: 74.12013475736507%
Precision(positive): 75.79285714285714%
Precision(negative): 72.43562077398936%

