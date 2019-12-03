run: Main.class
	@java Main

clean:
	rm -f Main.class

Main.class:
	@javac Main.java

