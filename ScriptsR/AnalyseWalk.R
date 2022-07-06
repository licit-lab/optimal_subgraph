walkMore <- read.table("Documents/eclipse-workspace/SubGraphs/WalkMore.txt")
pourcGagne <- read.table("Documents/eclipse-workspace/SubGraphs/WalkMore.txt")
walkMore <- subset(walkMore, walkMore[,1] > 0.01)
walkMore <- subset(walkMore, walkMore[,1] < 1000)
pourcGagne <- subset(pourcGagne, pourcGagne[,1] > 0.01)
walkMore <- subset(walkMore, walkMore[,2] > 0.01)
pourcGagne <- subset(pourcGagne, pourcGagne[,2] > 0.01)
#plot(walkMore[,2], walkMore[,1])

walkMoreCut <- subset(walkMore, walkMore[,2] > walkMore[,1])

pourcGagne[,1] <- walkMore[,1]
pourcGagne[,2] <- walkMore[,2]/walkMore[,1]

pourcGagne2 <- pourcGagne$V2
#pourcGagne2 <- pourcGagne2[pourcGagne2 < 200]
hist(pourcGagne2)
globalTimeWon <- walkMore$V2
walkMore1 <- walkMore$V1
walkMore1 <- walkMore1[walkMore2<1000]
globalTimeWon <- globalTimeWon[globalTimeWon<1000]

plot(pourcGagne2, pourcGagne$V1)
plot(globalTimeWon, walkMore$V1)
interestingWalkMore <- walkMore1[walkMore1 < globalTimeWon & walkMore1 < 5]
interestingGlobal <- globalTimeWon[walkMore1 < globalTimeWon & walkMore1 < 5]
length(interestingGlobal)/length(globalTimeWon)
plot(interestingWalkMore, interestingGlobal, xlim = c(0, 27), ylim = c(0,27))
plot(walkMore1, globalTimeWon, xlim = c(0, 27), ylim = c(0,27))
diffWalk <- globalTimeWon - walkMore$V1
diffWalk <- diffWalk[diffWalk < 1000]
hist(diffWalk)
summary(walkMore$V2-walkMore$V1)
