package riskdata.microservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.lucene.service.rest.exception.InvalidLuceneQueryException;
import edu.asu.lucene.service.rest.exception.LuceneSearcherException;
import edu.asu.lucene.service.rest.search.LuceneSearcher;
import riskdata.cnpj.BuscaUtil;
import riskdata.cnpj.rfb.DaoRfb;
import riskdata.cnpj.rfb.Empresa;

@RestController
public class BuscaServiceController extends BuscaIndiceServiceController {

	@Autowired
	private DaoRfb daoRfb;

	@RequestMapping(value = "/cnpj_util/razao_social", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public Resultado buscarPorRazaoSocial(@RequestParam(value = "q") String razaoSocial)
			throws LuceneSearcherException, InvalidLuceneQueryException {
		
		if (razaoSocial.trim().equals("")){
			return new Resultado(new HashMap<>(),"");
		}
		
		Map<String, List<String>> mapEmpresaToCnpjs = new HashMap<>();
		String descricao = BuscaUtil.processarDescricaoContratado(razaoSocial);
		List<Empresa> empresas = daoRfb.buscarEmpresaPorRazaoSocial(descricao);
		String tipoBusca = "";

		if (empresas.size() > 0) {
			tipoBusca = "BUSCA EXATA RFB";
			for (Empresa empresa : empresas) {
				List<String> cnpjs = mapEmpresaToCnpjs.get(empresa.getRazaoSocial());
				if (cnpjs == null) {
					cnpjs = new ArrayList<>();
					mapEmpresaToCnpjs.put(empresa.getRazaoSocial(), cnpjs);
				}
				cnpjs.add(empresa.getCnpj());
			}
		} else {
			tipoBusca = buscarNoIndice(descricao, mapEmpresaToCnpjs);
		}
		return new Resultado(mapEmpresaToCnpjs, tipoBusca);
	}

	
}
