package mtgdeckbuilder.frontend.swingworkers;

class StartedUpdate extends ProgressUpdate {

    private final int number;

    StartedUpdate(int number) {
        this.number = number;
    }

    @Override
    int getNumber() {
        return number;
    }

}
