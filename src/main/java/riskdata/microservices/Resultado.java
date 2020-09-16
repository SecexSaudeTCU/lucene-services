package riskdata.microservices;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "dados")
public class Resultado {
	private Map<String, List<String>> empresas;
	
	@JsonProperty("tipo_busca")
	private String tipoBusca;

	public Resultado(Map<String, List<String>> empresas, String tipoBusca) {
		super();
		this.empresas = empresas;
		this.tipoBusca = tipoBusca;
	}

	public Map<String, List<String>> getEmpresas() {
		return empresas;
	}

	public String getTipoBusca() {
		return tipoBusca;
	}
}
