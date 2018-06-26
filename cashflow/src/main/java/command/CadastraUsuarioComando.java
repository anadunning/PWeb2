package command;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.Resultado;
import controller.UsuarioController;
import dao.PersistenceUtil;
import model.Usuario;

public class CadastraUsuarioComando implements ICommand {
	
	@Override
	public Resultado execute(HttpServletRequest request, HttpServletResponse response) {
		final String paginaSucesso = "controller.do?op=conusu";
		final String paginaErro = "usuario/cadastro.jsp";
		
		UsuarioController usuarioCtrl = new UsuarioController(PersistenceUtil.getCurrentEntityManager());
		
		HttpSession session = request.getSession();
		Usuario email = (Usuario) session.getAttribute("email");
		
		Resultado resultado = usuarioCtrl.cadastre(email, request.getParameterMap());
		if (!resultado.isErro()) {
			resultado.setProximaPagina(paginaSucesso);
			resultado.setRedirect(true);
		} else {
			request.setAttribute("usuario", (Usuario) resultado.getModel());
			request.setAttribute("_msg", resultado.getMensagens());
			resultado.setProximaPagina(paginaErro);
		}
		return resultado;
	}
}