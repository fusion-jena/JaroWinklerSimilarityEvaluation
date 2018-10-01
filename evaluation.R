# dependencies
library(plyr)

# load data
data <- read.delim(file="results/benchmark.csv",header=TRUE,sep=",")

# format data
data$p5_coverage <- levels(data$p5_coverage)[data$p5_coverage]
data$p6_matcher <- levels(data$p6_matcher)[data$p6_matcher]
if(is.factor(data$measure)) {
  data$measure <- suppressWarnings(as.numeric(levels(data$measure)))[data$measure]
  data <- data[!is.na(data$measure),]
}

# copy naive unprepared .99 data to naive prepared and other thresholds
# (executed only once as there is no preparation step and no difference between thresholds)
dataNaive <- data[data$p6_matcher == "NAIVE",]
dataNaive$p3_threshold <- 0.95
data <- rbind(data,dataNaive)
dataNaive$p3_threshold <- 0.91
data <- rbind(data,dataNaive)
dataNaive$p4_prepared <- TRUE
dataNaive$p3_threshold <- 0.99
data <- rbind(data,dataNaive)
dataNaive$p3_threshold <- 0.95
data <- rbind(data,dataNaive)
dataNaive$p3_threshold <- 0.91
data <- rbind(data,dataNaive)
rm(dataNaive)

# get possible values
querySizes <- sort(unique(data$p1_querySize))
termSizes <- sort(unique(data$p2_termSize))
thresholds <- sort(unique(data$p3_threshold))
prepareds <- sort(unique(data$p4_prepared))
coverages <- sort(unique(data$p5_coverage))
matchers <- sort(unique(data$p6_matcher))
executions <- sort(unique(data$execution))
iterations <- sort(unique(data$iteration))

message("check difference of executions")
total <- length(querySizes) * length(termSizes) * length(thresholds) * length(prepareds) * length(coverages) * length(matchers) * length(executions)
progress <- 0
progressBar <- txtProgressBar(min = 0, max = total, style = 3)
executionDifferences <- NULL
for(querySize in querySizes) {
  for(termSize in termSizes) {
    for(threshold in thresholds) {
      for(prepared in prepareds) {
        for(coverage in coverages) {
          for(matcher in matchers) {
            for(executionX in executions) {
              measureX <- data[data$p1_querySize == querySize & data$p2_termSize == termSize &
                                 data$p3_threshold == threshold & data$p4_prepared == prepared &
                                 data$p5_coverage == coverage & data$p6_matcher == matcher &
                                 data$execution == executionX,]$measure
              for(executionY in executions) {
                if (executionX < executionY) {
                  try({
                    measureY <- data[data$p1_querySize == querySize & data$p2_termSize == termSize &
                                       data$p3_threshold == threshold & data$p4_prepared == prepared &
                                       data$p5_coverage == coverage & data$p6_matcher == matcher &
                                       data$execution == executionY,]$measure
                    # compair executions
                    t <- t.test(measureX,measureY)
                    if (t$p.value < 0.05) {
                      # store significant differences
                      executionDifferences <- rbind(executionDifferences,
                                                   data.frame(QuerySize=querySize,
                                                              TermSize=termSize,
                                                              Threshold=threshold,
                                                              Prepared=prepared,
                                                              Coverage=coverage,
                                                              Matcher=matcher,
                                                              ExecutionX=executionX,
                                                              ExecutionY=executionY,
                                                              MeanX=t$estimate["mean of x"],
                                                              MeanY=t$estimate["mean of y"],
                                                              Difference=t$estimate["mean of y"] - t$estimate["mean of x"],
                                                              Pvalue=t$p.value, 
                                                              stringsAsFactors=FALSE))
                    }
                  },silent = TRUE)
                }
              }
              progress <- progress + 1
              setTxtProgressBar(progressBar, progress)
            }
          }
        }
      }
    }
  }
}
close(progressBar)
row.names(executionDifferences) <- NULL
table(executionDifferences$Matcher)

message("remove iterations with missing executions")
data <- ddply(data,.(p1_querySize,p2_termSize,p3_threshold,p4_prepared,p5_coverage,p6_matcher,iteration),transform,count = length(measure),.progress = "text")
data <- data[data$count == 3,1:9]

message("remove executions with missing iterations")
data <- ddply(data,.(p1_querySize,p2_termSize,p3_threshold,p4_prepared,p5_coverage,p6_matcher,execution),transform,count = length(measure),.progress = "text")
data <- data[data$count == 20,1:9]

message("aggregate measure with median over executions")
data <- aggregate(measure ~ p1_querySize + p2_termSize + p3_threshold + p4_prepared + p5_coverage + p6_matcher + iteration, data, median)

message("generate result matrix by coverage")
total <- length(coverages) * length(matchers) * length(matchers) * length(querySizes) * length(termSizes)
expectedCount <- length(iterations)
progress <- 0
progressBar <- txtProgressBar(min = 0, max = total, style = 3)
for(coverage in coverages) {
  matrix <- data.frame()
  result <- data.frame()
  for(matcherX in matchers) {
    for(matcherY in matchers) {
      for(querySize in querySizes) {
        for(termSize in termSizes) {
          rowName <- paste(matcherY, " QS_", querySize, sep="")
          colName <- paste(matcherX, " TS_", termSize, sep="")
          matrix[rowName,colName] <- ""
          
          for(threshold in thresholds) {
            if (matcherX != matcherY) {
              prepared <- matcherX < matcherY
              
              symbol <- "!"
              greaterX <- FALSE
              greaterY <- FALSE
              significant <- FALSE
              
              
              measureX <- data[data$p1_querySize == querySize & data$p2_termSize == termSize &
                                 data$p3_threshold == threshold & data$p4_prepared == prepared &
                                 data$p5_coverage == coverage & data$p6_matcher == matcherX,]$measure
              measureY <- data[data$p1_querySize == querySize & data$p2_termSize == termSize &
                                 data$p3_threshold == threshold & data$p4_prepared == prepared &
                                 data$p5_coverage == coverage & data$p6_matcher == matcherY,]$measure
              
              complete <- length(measureX) == expectedCount & length(measureY) == expectedCount
              
              if(complete) {
                try({
                  t <- t.test(measureX,measureY)
                  
                  significant <- t$p.value < 0.05
                  
                  greaterX <- t$estimate["mean of x"] >= t$estimate["mean of y"]
                  greaterY <- t$estimate["mean of x"] <= t$estimate["mean of y"]
                  
                  if (!greaterX & !greaterY) {
                    symbol <- "="
                  } else if (greaterY) {
                    if (significant) {
                      symbol <- "<"
                    } else {
                      symbol <- "-"
                    }
                  } else if (greaterX) {
                    if (significant) {
                      symbol <- "^"
                    } else {
                      symbol <- "|"
                    }
                  } else {
                    symbol <- "!"
                  }
                },silent = TRUE)
              } else {
                symbol <- "?"
              }
              
              matrix[rowName,colName] <- paste(matrix[rowName,colName], symbol, sep=" ")
              
              result <- rbind(result,
                                  data.frame(querySize=querySize,
                                             termSize=termSize,
                                             threshold=threshold,
                                             prepared=prepared,
                                             matcherX=matcherX,
                                             matcherY=matcherY,
                                             complete=complete,
                                             significant=significant,
                                             greaterX=greaterX,
                                             greaterY=greaterY, 
                                             stringsAsFactors=FALSE))
            }
          }
          progress <- progress + 1
          setTxtProgressBar(progressBar, progress)
        }
      }
    }
  }
  assign(paste("matrix",coverage, sep = "_"), matrix)
  assign(paste("result",coverage, sep = "_"), result)
  rm(matrix,result)
}

message("check difference of coverages")
total <- length(querySizes) * length(termSizes) * length(thresholds) * length(prepareds) * length(coverages) * length(matchers)
progress <- 0
progressBar <- txtProgressBar(min = 0, max = total, style = 3)
coverageDifferences <- NULL
for(querySize in querySizes) {
  for(termSize in termSizes) {
    for(threshold in thresholds) {
      for(prepared in prepareds) {
        for(matcher in matchers) {
          for(coverageX in coverages) {
            measureX <- data[data$p1_querySize == querySize & data$p2_termSize == termSize &
                               data$p3_threshold == threshold & data$p4_prepared == prepared &
                               data$p5_coverage == coverageX & data$p6_matcher == matcher,]$measure
            for(coverageY in coverages) {
              if (coverageX < coverageY) {
                try({
                  measureY <- data[data$p1_querySize == querySize & data$p2_termSize == termSize &
                                     data$p3_threshold == threshold & data$p4_prepared == prepared &
                                     data$p5_coverage == coverageY & data$p6_matcher == matcher,]$measure
                  # compair executions
                  t <- t.test(measureX,measureY)
                  if (t$p.value < 0.05) {
                    # store significant differences
                    coverageDifferences <- rbind(coverageDifferences,
                                                  data.frame(QuerySize=querySize,
                                                             TermSize=termSize,
                                                             Threshold=threshold,
                                                             Prepared=prepared,
                                                             CoverageX=coverageX,
                                                             CoverageY=coverageY,
                                                             Matcher=matcher,
                                                             MeanX=t$estimate["mean of x"],
                                                             MeanY=t$estimate["mean of y"],
                                                             Difference=t$estimate["mean of y"] - t$estimate["mean of x"],
                                                             Pvalue=t$p.value, 
                                                             stringsAsFactors=FALSE))
                  }
                },silent = TRUE)
              }
            }
            progress <- progress + 1
            setTxtProgressBar(progressBar, progress)
          }
        }
      }
    }
  }
}
close(progressBar)
row.names(coverageDifferences) <- NULL
table(coverageDifferences$Matcher)

message("generate result matrix for mixed coverages")
total <- length(matchers) * length(matchers) * length(querySizes) * length(termSizes)
expectedCount <- length(iterations) * length(coverages)
progress <- 0
progressBar <- txtProgressBar(min = 0, max = total, style = 3)
matrix_ALL <- data.frame()
result_ALL <- data.frame()
for(matcherX in matchers) {
  for(matcherY in matchers) {
    for(querySize in querySizes) {
      for(termSize in termSizes) {
        rowName <- paste(matcherY, " QS_", querySize, sep="")
        colName <- paste(matcherX, " TS_", termSize, sep="")
        matrix_ALL[rowName,colName] <- ""
        
        for(threshold in thresholds) {
          if (matcherX != matcherY) {
            prepared <- matcherX < matcherY
            
            symbol <- "!"
            greaterX <- FALSE
            greaterY <- FALSE
            significant <- FALSE
            
            measureX <- subset(data, p1_querySize == querySize & p2_termSize == termSize & p3_threshold == threshold & 
                                 p4_prepared == prepared & p6_matcher == matcherX)$measure
            measureY <- subset(data, p1_querySize == querySize & p2_termSize == termSize & p3_threshold == threshold & 
                                 p4_prepared == prepared & p6_matcher == matcherY)$measure
            
            complete <- length(measureX) == expectedCount & length(measureY) == expectedCount
            
            if(complete) {
              try({
                t <- t.test(measureX,measureY)
                
                significant <- t$p.value < 0.05
                
                greaterX <- t$estimate["mean of x"] >= t$estimate["mean of y"]
                greaterY <- t$estimate["mean of x"] <= t$estimate["mean of y"]
                
                if (!greaterX & !greaterY) {
                  symbol <- "="
                } else if (greaterY) {
                  if (significant) {
                    symbol <- "<"
                  } else {
                    symbol <- "-"
                  }
                } else if (greaterX) {
                  if (significant) {
                    symbol <- "^"
                  } else {
                    symbol <- "|"
                  }
                } else {
                  symbol <- "!"
                }
                
              },silent = TRUE)
            } else {
              symbol <- "?"
            }
            
            matrix_ALL[rowName,colName] <- paste(matrix_ALL[rowName,colName], symbol, sep=" ")
            
            result_ALL <- rbind(result_ALL,
                                         data.frame(querySize=querySize,
                                                    termSize=termSize,
                                                    threshold=threshold,
                                                    prepared=prepared,
                                                    matcherX=matcherX,
                                                    matcherY=matcherY,
                                                    complete=complete,
                                                    significant=significant,
                                                    greaterX=greaterX,
                                                    greaterY=greaterY, 
                                                    stringsAsFactors=FALSE))
          }
        }
        progress <- progress + 1
        setTxtProgressBar(progressBar, progress)
      }
    }
  }
}
close(progressBar)



message("generate LaTex tables")

formatedSize <- function(size) {
  if (size == 1) {
    return("\\(1\\)")
  }else if(size == 10) {
    return("\\(10\\)")
  }else{
    return(paste0("\\(10^",log10(size),"\\)"))
  }
}

formatedMatcher <- function(matcher) {
  matcherLookup <- NULL
  matcherLookup["TRIE"] <- "Our"
  matcherLookup["NAIVE"] <- "Naive"
  matcherLookup["QUICK"] <- "Dresler"
  return(matcherLookup[matcher])
}

lMultirow <- function(rows, content, width="*") {
  return(paste0("\\multirow{",rows,"}{",width,"}{",content,"}"))
}

lMulticolumn <- function(cols, content, align="c|") {
  return(paste0("\\multicolumn{",cols,"}{",align,"}{",content,"}"))
}

lrotat <-function(content,angle=90,origin="c"){
  return(paste0("\\rotatebox[origin=",origin,"]{",angle,"}{",content,"}"))
}

lCell <- function(content) {
  return(paste0("&",content))
}

matrix2LaTex <- function(file,result) {
  sink(file=file)
  
  #header
  cat("\\begin{tabular}{|c|r|")
  for(matcherX in matchers) {
    for(termSize in termSizes) {
      for(threshold in thresholds) {
        cat("c")
      }
      cat("|")
    }
  }
  cat("c|}\n")
  cat("\\hline\n")
  cat(lMulticolumn(2,"Queries \\(\\triangledown\\)",align="|c|"))
  for(matcherX in matchers) {
    cat(lCell(lMulticolumn(length(termSizes)*length(thresholds),formatedMatcher(matcherX))))
  }
  cat("&\\\\\n")
  cat("\\hline\n")
  cat(lMulticolumn(2,"Terms \\(\\vartriangleright\\)",align="|c|"))
  for(matcherX in matchers) {
    for(termSize in termSizes) {
      cat(lCell(lMulticolumn(length(thresholds),formatedSize(termSize))))
    }
  }
  cat("&\\\\\n")
  cat(lMulticolumn(2,"Threshold",align="|r|"))
  for(matcherX in matchers) {
    for(termSize in termSizes) {
      for(threshold in thresholds) {
        cat("&",sub("^(-?)0.", "\\1.", sprintf("%.2f", threshold)))
      }
    }
  }
  cat("&\\\\\n")
  
  #rows
  for(matcherY in matchers) {
    for(querySize in querySizes) {
      if(querySize == querySizes[1]) { # first row of matcher
        if (matcherY != matchers[1] && matcherY != matchers[length(matchers)]) { # not first or last matcher
          cat(paste0("\\cline{1-",length(matchers)*length(termSizes)*length(thresholds)+2,"}\n"))
        } else {
          cat("\\hline\n")
        }
        cat(lMultirow(length(querySizes),lrotat(formatedMatcher(matcherY))))
      }
      cat("&",formatedSize(querySize))
      for(matcherX in matchers) {
        for(termSize in termSizes) {
          for(threshold in thresholds) {
            #message(querySize, " ", termSize, " ", threshold, " ", matcherX, " ", matcherY)
            if (matcherX != matcherY) {
              cell <- result[result$querySize==querySize &
                                   result$termSize==termSize & 
                                   result$matcherX==matcherX &
                                   result$matcherY==matcherY &
                                   result$threshold==threshold,]
              #print(cell)
              if (cell$complete) {
                if (!cell$greaterX & !cell$greaterY) {
                  symbol <- "\\(\\approx\\)"
                } else if (cell$greaterY) {
                  if (cell$significant) {
                    symbol <- "\\LEFTarrow"
                  } else {
                    symbol <- "(\\LEFTarrow)"
                  }
                } else if (cell$greaterX) {
                  if (cell$significant) {
                    symbol <- "\\(\\vartriangle\\)"
                  } else {
                    symbol <- "\\((\\vartriangle)\\)"
                  }
                } else {
                  symbol <- "!"
                }
              } else {
                symbol <- "?"
              }
              cat("& ")
              cat(symbol)
              cat(" ")
            } else {
              cat("& ~ ")
            }
          }
        }
      }
      if(matcherY == matchers[1] && querySize == querySizes[1]) { # first row of first matcher
        cat(lCell(lMultirow((length(matchers)-1)*length(querySizes),lrotat("unprepared"))))
      } else {
        cat("&")
      }
      cat("\\\\\n")
    }
  }
  cat("\\hline\n")
  cat("&")
  cat(lCell(lMulticolumn((length(matchers)-1)*length(termSizes)*length(thresholds),"prepared")))
  cat(lCell(lMulticolumn(length(termSizes)*length(thresholds),"")))
  cat("&\\\\\n")
  cat("\\hline\n")
  cat("\\end{tabular}")
  
  sink()
}

matrix2LaTex("paper-evaluation-results-table-all.tex",result_ALL)
matrix2LaTex("paper-evaluation-results-table-full.tex",result_FULL)
matrix2LaTex("paper-evaluation-results-table-half.tex",result_HALF)
matrix2LaTex("paper-evaluation-results-table-none.tex",result_NONE)

comparisonPlot <- function(data) {
  par(mar=c(4,4,0.1,1))
  
  # aggregate data
  data <- aggregate(measure ~ p1_querySize + p6_matcher, data, mean)
  
  # get the range for the x and y axis
  xrange <- range(querySizes)
  yrange <- range(data$measure)
  
  suppressWarnings(plot(NULL,xlim=xrange, ylim=yrange, type="b", xlab="Number of Queries", ylab="Throughput (ops/s)", log="xy", axes=FALSE, frame.plot=TRUE))
  axis(1,at=querySizes,labels=as.character(querySizes))
  axis(2,at=10^(-3:5),labels=c(0.001,0.01,0.1,1,10,100,1000,10000,100000))
  matchersFormated <- sapply(matchers, formatedMatcher, simplify = "array", USE.NAMES = FALSE)
  legend(xrange[2], yrange[2], matchersFormated, cex=0.8, lty=1:length(matchers), pch=(1:length(matchers))+1, bty="n",xjust=1)
  
  # add lines
  for (i in 1:length(matchers)) {
    matcherData <- subset(data, p6_matcher==matchers[i])
    lines(matcherData$measure ~ matcherData$p1_querySize, type="b", lty=i, pch=i+1)
  }
}

pdf("plot_ts6_th91_p0.pdf", width=5, height=4)
comparisonPlot(subset(data, p2_termSize == 1000000 & p3_threshold == 0.91 & p4_prepared == FALSE))
dev.off()

pdf("plot_ts6_th95_p0.pdf", width=5, height=4)
comparisonPlot(subset(data, p2_termSize == 1000000 & p3_threshold == 0.95 & p4_prepared == FALSE))
dev.off()

pdf("plot_ts6_th99_p0.pdf", width=5, height=4)
comparisonPlot(subset(data, p2_termSize == 1000000 & p3_threshold == 0.99 & p4_prepared == FALSE))
dev.off()

pdf("plot_ts6_th91_p1.pdf", width=5, height=4)
comparisonPlot(subset(data, p2_termSize == 1000000 & p3_threshold == 0.91 & p4_prepared == TRUE))
dev.off()

pdf("plot_ts6_th95_p1.pdf", width=5, height=4)
comparisonPlot(subset(data, p2_termSize == 1000000 & p3_threshold == 0.95 & p4_prepared == TRUE))
dev.off()

pdf("plot_ts6_th99_p1.pdf", width=5, height=4)
comparisonPlot(subset(data, p2_termSize == 1000000 & p3_threshold == 0.99 & p4_prepared == TRUE))
dev.off()
