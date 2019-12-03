FROM maven:3-jdk-8-slim

RUN apt update && apt install git -y
RUN git clone https://github.com/codenjoyme/codenjoy.git /codenjoy && \
    sed -i 's/period = 1000;/period = 50;/' /codenjoy/CodingDojo/server/src/main/java/com/codenjoy/dojo/services/TimerService.java && \
    sed -i 's/print("Board: " + board);//' /codenjoy/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/client/WebSocketRunner.java && \
    sed -i 's/print("Answer: " + answer);//' /codenjoy/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/client/WebSocketRunner.java && \
    sed -i 's/String playerId = validator.checkPlayerCode(playerName, code);//' /codenjoy/CodingDojo/server/src/main/java/com/codenjoy/dojo/web/controller/BoardController.java && \
    sed -i 's/Player player = playerService.get(playerId);/Player player = playerService.get(playerName);/' /codenjoy/CodingDojo/server/src/main/java/com/codenjoy/dojo/web/controller/BoardController.java

WORKDIR /codenjoy/CodingDojo

RUN cd games/engine && mvn install -DskipTests
RUN cd server && mvn install -DskipTests
RUN cd builder && mvn install -DskipTests

CMD cd builder && mvn jetty:run-war -Pbattlecity
