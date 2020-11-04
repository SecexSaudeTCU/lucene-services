package riskdata.microservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.lucene.service.rest.exception.InvalidLuceneQueryException;
import edu.asu.lucene.service.rest.exception.LuceneSearcherException;
import edu.asu.lucene.service.rest.search.Result;

@RestController
public class BuscaIndiceServiceController {

	@Autowired
	BuscaIndice buscaIndice;

	@RequestMapping(value = "/cnpj_util/razao_social/indice", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public Resultado buscarPorRazaoSocial(@RequestParam(value = "q") String razaoSocial)
			throws LuceneSearcherException, InvalidLuceneQueryException {
		Map<String, List<String>> mapEmpresaToCnpjs = new HashMap<>();
		String tipoBusca = buscarNoIndice(razaoSocial, mapEmpresaToCnpjs);
		return new Resultado(mapEmpresaToCnpjs, tipoBusca);
	}

	protected String buscarNoIndice(String razaoSocial, Map<String, List<String>> mapEmpresaToCnpjs)
			throws LuceneSearcherException, InvalidLuceneQueryException {
		String tipoBusca;
		Result resultadosNoIndice = this.buscaIndice.buscarPorRazaoSocial(razaoSocial, true);
		if (resultadosNoIndice != null) {
			List<Map<String, String>> records = resultadosNoIndice.getRecords();

			if (records.size() > 0) {
				tipoBusca = "BUSCA EXATA ÍNDICE";
			} else {
				tipoBusca = "BUSCA APROXIMADA ÍNDICE";
				resultadosNoIndice = this.buscaIndice.buscarPorRazaoSocial(razaoSocial, false);
				records = resultadosNoIndice.getRecords();
			}
			preencher(mapEmpresaToCnpjs, records);
			return tipoBusca;
		}
		return "";
	}

	private void preencher(Map<String, List<String>> mapEmpresaToCnpjs, List<Map<String, String>> records) {
		for (Map<String, String> map : records) {
			String razao_social = map.get("razao_social");
			String cnpj = map.get("cnpj");
			List<String> cnpjs = mapEmpresaToCnpjs.get(razao_social);
			if (cnpjs == null) {
				cnpjs = new ArrayList<>();
				mapEmpresaToCnpjs.put(razao_social, cnpjs);
			}
			cnpjs.add(cnpj);
		}
	}

}