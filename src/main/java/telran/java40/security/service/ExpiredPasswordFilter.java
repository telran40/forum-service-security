package telran.java40.security.service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import telran.java40.accounting.dao.UserAccountRepository;
import telran.java40.accounting.model.UserAccount;

@Service
public class ExpiredPasswordFilter extends GenericFilterBean {

	UserAccountRepository repository;

	@Autowired
	public ExpiredPasswordFilter(UserAccountRepository repository) {
		this.repository = repository;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		Principal principal = request.getUserPrincipal();
		if (principal != null && checkEndPoint(request.getMethod(), request.getServletPath())) {
//			UserProfile userProfile = (UserProfile) principal;
//			if (!userProfile.isPasswordNotExpired()) {
//				response.sendError(403, "Password expired");
//				return;
//			}
			UserAccount userAccount = repository.findById(principal.getName()).get();
			if (userAccount.getPasswordExpDate().isBefore(LocalDate.now())) {
				response.sendError(403, "Password expired");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		return !("Put".equalsIgnoreCase(method) && path.matches("/account/password/?"));
	}

}
