package mtgdeckbuilder.frontend.swingworkers;

class PartDoneUpdate extends ProgressUpdate {

    private final int number;

    PartDoneUpdate(int number) {
        this.number = number;
    }

    @Override
    int getNumber() {
        return number;
    }

}
