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
import edu.asu.lucene.service.rest.search.Result;
import riskdata.cnpj.BuscaUtil;
import riskdata.cnpj.rfb.DaoRfb;
import riskdata.cnpj.rfb.Empresa;

@RestController
public class IndiceServiceController {
	@Autowired
	private LuceneSearcher indexSearcher;

	@Value("${lucene.query.default.records}")
	private Integer QUERY_DEFAULT_RECORDS;

	@Value("${lucene.query.max.records}")
	private Integer QUERY_MAX_RECORDS;
	
	@Autowired
	private DaoRfb daoRfb;

	private final static Logger logger = Logger.getLogger("IndiceServiceController");

	@RequestMapping(value = "/cnpj_util/razao_social", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public Resultado buscarPorRazaoSocial(@RequestParam(value = "q") String razaoSocial)
			throws LuceneSearcherException, InvalidLuceneQueryException {
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
			Result resultadosNoIndice = buscarPorRazaoSocialLucene(razaoSocial, true);
			List<Map<String, String>> records = resultadosNoIndice.getRecords();

			if (records.size() > 0) {
				tipoBusca = "BUSCA EXATA ÍNDICE";
			}else {
				tipoBusca = "BUSCA APROXIMADA ÍNDICE";
				resultadosNoIndice = buscarPorRazaoSocialLucene(razaoSocial, false);
				records = resultadosNoIndice.getRecords();
			}
			preencher(mapEmpresaToCnpjs, records);
		}
		return new Resultado(mapEmpresaToCnpjs, tipoBusca);
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

	private Result buscarPorRazaoSocialLucene(String razaoSocial, boolean buscaExata)
			throws LuceneSearcherException, InvalidLuceneQueryException {
		String query = "razao_social:";
		if (buscaExata) {
			query += "\"" + razaoSocial + "\"";
		} else {
			razaoSocial = razaoSocial.replace("/", "\\/");
			query += razaoSocial;
		}

		int count = QUERY_DEFAULT_RECORDS;
		boolean showAvailable = false;

		Result results = indexSearcher.searchIndex(query, count, showAvailable);
		logger.info("Search for '" + query + "' found " + results.getAvailable() + " and retrieved "
				+ results.getRetrieved() + " records");
		return results;
	}

}
