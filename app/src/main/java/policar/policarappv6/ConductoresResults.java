package policar.policarappv6;

/**
 * Created by USUARIO on 02/07/2015.
 */
public class ConductoresResults {

    private String ConductorId = "";
    private String ConductorNombre = "";
    private String ConductorCanal = "";
    private String ConductorVehiculoUnidad = "";
    private String ConductorVehiculoPlaca = "";
    private String ConductorFoto = "";


    public void setConductorId(String ConductorId) {
        this.ConductorId = ConductorId;
    }

    public String getConductorId() {
        return ConductorId;
    }


    public void setConductorNombre(String ConductorNombre) {
        this.ConductorNombre = ConductorNombre;
    }

    public String getConductorNombre() {
        return ConductorNombre;
    }


    public void setConductorCanal(String ConductorCanal) {
        this.ConductorCanal = ConductorCanal;
    }

    public String getConductorCanal() {
        return ConductorCanal;
    }



    public void setConductorVehiculoUnidad(String ConductorVehiculoUnidad) {
        this.ConductorVehiculoUnidad = ConductorVehiculoUnidad;
    }

    public String getConductorVehiculoUnidad() {
        return ConductorVehiculoUnidad;
    }



    public void setConductorVehiculoPlaca(String ConductorVehiculoPlaca) {
        this.ConductorVehiculoPlaca = ConductorVehiculoPlaca;
    }

    public String getConductorVehiculoPlaca() {
        return ConductorVehiculoPlaca;
    }


    public String getConductorFoto() {
        return ConductorFoto;
    }

    public void setConductorFoto(String conductorFoto) {
        ConductorFoto = conductorFoto;
    }
}
