package dom;

import utils.Ciudades;

public class Main {
    public static void main(String[] args) throws Exception {
        ManejadorDOM manejadorDOM = new ManejadorDOM();

        manejadorDOM.generarSitiosInteres(Ciudades.LORCA.getValue());
        manejadorDOM.generarSitiosInteres(Ciudades.MALAGA.getValue());
    }
}
