package com.example.timetrack.ui.pages;

import com.example.timetrack.entity.User;
import com.example.timetrack.services.UserService;
import com.example.timetrack.ui.RootLayout;
import com.example.timetrack.ui.uitls.Utils;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.util.Strings;

import java.util.Objects;

@PageTitle("Настройки пользователя")
@Route(value = "usersettings", layout = RootLayout.class)
public class UserSettingsPage extends VerticalLayout implements DefaultPage {

    private final UserService userService;
    private final User user = (User) VaadinSession.getCurrent().getAttribute("user");

    private TextField nameField = new TextField("Имя");
    private TextField secondNameField = new TextField("Фамилия");
    private DatePicker dateField = new DatePicker("Дата рождения");
    private PasswordField currentPasswordField = new PasswordField("Текущий пароль");
    private PasswordField firstPasswordField = new PasswordField("Новый пароль");
    private PasswordField secondPasswordField = new PasswordField("Повтор нового пароля");

    private Binder<User> binder = new Binder<>(User.class);

    public UserSettingsPage(UserService userService) {
        this.userService = userService;

        initContent();
        bindData();
    }

    private void bindData() {
        binder.readBean(user);
    }

    private void initContent() {
        initFields();
        Card card = new Card();
        card.setSizeFull();

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

        HorizontalLayout nameLayout = new HorizontalLayout();
        nameLayout.setWidthFull();
        nameLayout.add(nameField, secondNameField);

        mainLayout.add(nameLayout, dateField, currentPasswordField, firstPasswordField, secondPasswordField);
        card.add(mainLayout);

        Button saveButton = new Button("Сохранить", this::save);

        HorizontalLayout toolBar = new HorizontalLayout(saveButton);
        toolBar.setWidthFull();
        toolBar.setJustifyContentMode(JustifyContentMode.END);
        add(card, toolBar);
    }

    private void save(ClickEvent<Button> event) {
        if (binder.writeBeanIfValid(user)) {
            if (!Strings.isBlank(currentPasswordField.getValue())) {
                if (!Objects.equals(user.getPassword(), currentPasswordField.getValue())) {
                    currentPasswordField.setInvalid(true);
                    currentPasswordField.setErrorMessage("Неверный пароль");
                    return;
                }
                if (!Strings.isBlank(firstPasswordField.getValue())) {
                    String firstPassword = this.firstPasswordField.getValue();
                    if (!Strings.isBlank(secondPasswordField.getValue())) {
                        if (firstPassword.equals(secondPasswordField.getValue())) {
                            user.setPassword(firstPassword);
                        } else {
                            firstPasswordField.setInvalid(true);
                            firstPasswordField.setErrorMessage("Пароли не совпадают");
                            secondPasswordField.setInvalid(true);
                            secondPasswordField.setErrorMessage("Пароли не совпадают");
                            return;
                        }
                    } else {
                        secondPasswordField.setInvalid(true);
                        secondPasswordField.setErrorMessage("Поле не может быть пустым");
                        return;
                    }
                } else {
                    firstPasswordField.setInvalid(true);
                    firstPasswordField.setErrorMessage("Поле не может быть пустым");
                    return;
                }
            } else {
                currentPasswordField.setInvalid(false);
                firstPasswordField.setInvalid(false);
                secondPasswordField.setInvalid(false);
            }
            currentPasswordField.clear();
            firstPasswordField.clear();
            secondPasswordField.clear();
            userService.save(user);
            Utils.showSuccessNotification("Данные успешно сохранены");
        }
    }

    private void initFields() {
        binder.forField(nameField)
                .asRequired("Поле не должно быть пустым")
                .bind(User::getName, User::setName);
        nameField.setWidthFull();

        binder.forField(secondNameField)
                .asRequired("Поле не должно быть пустым")
                .bind(User::getSecondName, User::setSecondName);
        secondNameField.setWidthFull();

        binder.forField(dateField)
                .asRequired("Поле не должно быть пустым")
                .bind(User::getDateOfBirth, User::setDateOfBirth);
        dateField.setWidthFull();

        currentPasswordField.setWidthFull();
        firstPasswordField.setWidthFull();
        secondPasswordField.setWidthFull();
    }
}
