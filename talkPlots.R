library(ggplot2)
library(cowplot)

talkFormatedMatcher <- function(matcher) {
  matcherLookup <- NULL
  matcherLookup["TRIE"] <- "Our"
  matcherLookup["NAIVE"] <- "Naive"
  matcherLookup["QUICK"] <- "Dreßler"
  return(matcherLookup[matcher])
}

talkPanelLabelFormat <- function(x){paste("Threshold =", x)}

talkData <- subset(data, p2_termSize == 1000000 & p4_prepared == TRUE) # filter data
talkData <- aggregate(measure ~ p1_querySize + p3_threshold + p6_matcher, talkData, mean) # aggregate data
talkData["p6_matcher"] <- lapply(talkData["p6_matcher"],talkFormatedMatcher)
talkData["p3_threshold"] <- lapply(talkData["p3_threshold"],talkPanelLabelFormat)

talkAxisFormat <- function(x){format(x,scientific=FALSE,big.mark=",")}

talkLabelsX <- c("1","10",parse(text="10^2"),parse(text="10^3"),parse(text="10^4"),parse(text="10^5"))
talkLabelsY <- c("0.1","1","10",parse(text="10^2"),parse(text="10^3"))

talkPlot <- ggplot(talkData, aes(x=p1_querySize, y=1000/measure/p1_querySize, group=p6_matcher)) +
  xlim(0, 100000) +
  ylim(0, 1500) +
  scale_x_continuous(trans='log10',labels=talkLabelsX,breaks=10^(0:5)) +
  scale_y_continuous(trans='log10',labels=talkLabelsY,breaks=10^(-1:3)) +
  geom_point(aes(colour=p6_matcher), size=3) +
  geom_line(aes(colour=p6_matcher), size=1) +
  scale_color_brewer(palette="Set1") +
  theme(legend.position=c(.0, .175),axis.text.x=element_text(vjust=0)) +
  labs(x="Number of Queries", y="Mean Time per Query (ms)", colour="Approach") +
  facet_grid(. ~ p3_threshold) + # 
  background_grid(major = 'xy', minor = "none") + # add thin horizontal lines 
  panel_border() #add border around each panel

ggsave("plot_ts6_p1_talk.pdf", plot=talkPlot , width=9, height=4)
