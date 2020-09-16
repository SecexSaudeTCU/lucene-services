package riskdata.cnpj;

import java.text.Normalizer;

public class BuscaUtil {
	public static String processarDescricaoContratado(String descricao) {
		descricao = descricao.trim().toUpperCase();

		// Remove espaços extras.
		descricao = descricao.replaceAll(" +", " ");

		// Remove acentos.
		descricao = stripAccents(descricao);

		// Remove caracteres especiais
		descricao = descricao.replace("&", "").replace("/", "").replace("-", "").replace("\"", "");
		
		return descricao;
	}

	private static String stripAccents(String s) {
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return s;
	}
}
