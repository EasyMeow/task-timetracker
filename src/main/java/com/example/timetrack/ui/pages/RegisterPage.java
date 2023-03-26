package com.example.timetrack.ui.pages;

import com.example.timetrack.entity.User;
import com.example.timetrack.enums.Position;
import com.example.timetrack.services.UserService;
import com.example.timetrack.ui.uitls.Utils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.util.Strings;

@PageTitle("Register")
@Route(value = "register")
public class RegisterPage extends VerticalLayout implements DefaultPage, HasUrlParameter<String> {

    private String urlParam = "";

    private final UserService userService;

    private final Dialog dialog = new Dialog();
    private TextField nameField;
    private TextField secondNameField;
    private DatePicker birthDateField;
    private Select<Position> positionField;
    private TextField loginField;
    private PasswordField passwordField;
    private PasswordField retryPasswordField;

    public RegisterPage(UserService userService) {
        this.userService = userService;

        initContent();
    }

    private void initContent() {
        nameField = new TextField();
        nameField.setWidthFull();
        nameField.setLabel("Имя");

        secondNameField = new TextField();
        secondNameField.setWidthFull();
        secondNameField.setLabel("Фамилия");

        birthDateField = new DatePicker();
        birthDateField.setWidthFull();
        birthDateField.setLabel("Дата рождения");

        positionField = new Select<>();
        positionField.setEmptySelectionAllowed(false);
        positionField.setWidthFull();
        positionField.setLabel("Должность");

        loginField = new TextField();
        loginField.setWidthFull();
        loginField.setLabel("Логин");

        passwordField = new PasswordField();
        passwordField.setWidthFull();
        passwordField.setLabel("Пароль");

        retryPasswordField = new PasswordField();
        retryPasswordField.setWidthFull();
        retryPasswordField.setLabel("Повторите пароль");

        Button registerButton = new Button("Зарегистрироваться", this::register);
        registerButton.setWidthFull();

        HorizontalLayout nameBar = new HorizontalLayout(nameField, secondNameField);
        nameBar.setWidthFull();
        HorizontalLayout datePositionBar = new HorizontalLayout(birthDateField, positionField);
        datePositionBar.setWidthFull();

        VerticalLayout dialogLayout = new VerticalLayout(nameBar, datePositionBar, loginField, passwordField, retryPasswordField, registerButton);

        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void register(ClickEvent<Button> event) {
        if(validate()) {
            User user = new User();
            user.setName(nameField.getValue());
            user.setSecondName(secondNameField.getValue());
            user.setDateOfBirth(birthDateField.getValue());
            user.setPosition(positionField.getValue());
            user.setLogin(loginField.getValue());
            user.setPassword(passwordField.getValue());

            userService.save(user);

            forward();
        }
    }

    private boolean validate() {
        boolean isValid = true;
        if (Strings.isBlank(nameField.getValue())) {
            isValid = false;
            nameField.setInvalid(true);
            nameField.setErrorMessage("Имя не может быть пустым");
        }
        if (Strings.isBlank(secondNameField.getValue())) {
            isValid = false;
            secondNameField.setInvalid(true);
            secondNameField.setErrorMessage("Фамилия не может быть пустой");
        }
        if (birthDateField.getValue() == null) {
            isValid = false;
            birthDateField.setInvalid(true);
            birthDateField.setErrorMessage("Дата рождения не может быть пустой");
        }
        if (Strings.isBlank(loginField.getValue())) {
            isValid = false;
            loginField.setInvalid(true);
            loginField.setErrorMessage("Логин не может быть пустым");
        }
        if (Strings.isBlank(passwordField.getValue())) {
            isValid = false;
            passwordField.setInvalid(true);
            passwordField.setErrorMessage("Пароль не может быть пустым");
        }
        if (Strings.isBlank(retryPasswordField.getValue())) {
            isValid = false;
            retryPasswordField.setInvalid(true);
            retryPasswordField.setErrorMessage("Пароль не может быть пустым");
        }
        if (!passwordField.getValue().equals(retryPasswordField.getValue())) {
            isValid = false;
            Utils.showErrorNotification("Пароли не совпадают");
        } else if (userService.checkLoginExists(loginField.getValue())) {
            isValid = false;
            Utils.showErrorNotification("Пользователь с таким логином уже зарегистрирован");
        }

        return isValid;
    }

    private void forward() {
        getUI().ifPresent(ui -> ui.navigate("tasks"));
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        urlParam = s;
        positionField.setItems(urlParam.equals("pm") ? Position.PROJECT_MANAGER : (urlParam.equals("tm") ? Position.TEAM_LEAD : Position.DEVELOPER));
        positionField.setValue(urlParam.equals("pm") ? Position.PROJECT_MANAGER : (urlParam.equals("tm") ? Position.TEAM_LEAD : Position.DEVELOPER));
    }
}
