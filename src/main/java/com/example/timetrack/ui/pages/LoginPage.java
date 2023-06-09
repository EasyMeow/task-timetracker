package com.example.timetrack.ui.pages;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.User;
import com.example.timetrack.services.ProjectService;
import com.example.timetrack.services.UserService;
import com.example.timetrack.ui.uitls.Utils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.util.Strings;

@PageTitle("Login")
@Route(value = "login")
@CssImport("./styles/login.css")
public class LoginPage extends VerticalLayout implements DefaultPage, HasUrlParameter<String> {

    private final UserService userService;
    private final ProjectService projectService;

    private TextField loginField;
    private PasswordField passwordField;

    private String forwardTo = "";
    private final Dialog dialog = new Dialog();

    public LoginPage(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;

        initContent();
    }

    private void initContent() {
        loginField = new TextField();
        loginField.setWidthFull();
        loginField.setLabel("Логин");
        passwordField = new PasswordField();
        passwordField.setWidthFull();
        passwordField.setLabel("Пароль");


        Button enterButton = new Button("Войти", this::enter);
        enterButton.setWidthFull();

        Label label = new Label("Введите логин и пароль");
        label.getStyle().set("font-weight", "bold");
        Anchor registerAnchor = new Anchor("register/pm", "Зарегистрироваться", AnchorTarget.BLANK);

        HorizontalLayout toolbar = new HorizontalLayout(enterButton, registerAnchor);
        toolbar.setWidthFull();
        toolbar.getStyle().set("align-items", "baseline");
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(label, loginField, passwordField, toolbar);
        dialogLayout.addClassName("dialog-login");

        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void enter(ClickEvent<Button> event) {
        String login = loginField.getValue();
        String password = passwordField.getValue();
        if (Strings.isBlank(login)) {
            loginField.setInvalid(true);
            loginField.setErrorMessage("Логин не может быть пустым");
        }
        if (Strings.isBlank(password)) {
            passwordField.setInvalid(true);
            passwordField.setErrorMessage("Пароль не может быть пустым");
        } else {
            if (userService.checkPassword(login, password)) {
                User user = userService.authorize(login);
                VaadinSession.getCurrent().setAttribute("user", user);

                Project project = projectService.findFirstByUser(user);
                VaadinSession.getCurrent().setAttribute("project", project);

                forward();
                dialog.close();
            } else {
                Utils.showErrorNotification("Некорректный логин или пароль");
            }
        }
    }

    private void forward() {
        if (Strings.isBlank(forwardTo)) {
            forwardTo = "tasks";
        }
        getUI().ifPresent(ui -> ui.navigate(forwardTo));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        forwardTo = parameter;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Object user = VaadinSession.getCurrent().getAttribute("user");
        if (user != null) {
            if (Strings.isBlank(forwardTo)) {
                forwardTo = "tasks";
            }
            event.forwardTo(forwardTo);
        }
    }

}
