package riskdata.cnpj.rfb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository("DaoRfb")
public class DaoRfb {
	@Value("${sqlite.arquivo}")
	private String arquivoDb;

	private Connection getConexao() {
		// db parameters
		String url = "jdbc:sqlite:" + arquivoDb;
		try {			
			Connection conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
			return conn;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Empresa> buscarEmpresaPorRazaoSocial(String nome) {
		List<Empresa> resultado = new ArrayList<>();
		String sql = "SELECT cnpj, razao_social FROM empresas WHERE razao_social = ?";

		try (Connection conexao = getConexao()) {
			try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
				stmt.setString(1, nome);
				ResultSet rs = stmt.executeQuery();

				while (rs.next()) {
					resultado.add(new Empresa(rs.getString("cnpj"), rs.getString("razao_social")));
				}

				return resultado;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
