package com.example.timetrack.ui.pages.team;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Team;
import com.example.timetrack.entity.User;
import com.example.timetrack.enums.Position;
import com.example.timetrack.mail.EmailSender;
import com.example.timetrack.mail.Message;
import com.example.timetrack.services.TeamService;
import com.example.timetrack.ui.uitls.Utils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class InviteDialog extends Dialog {

    private EmailField emailField = new EmailField();
    private Select<Position> positionSelect = new Select<>();
    private Button send = new Button();

    private final TeamService teamService;
    private final EmailSender emailSender;
    private final User user;
    private final Team team;
    private final Project project;

    public InviteDialog(TeamService teamService, EmailSender emailSender, User user, Team team, Project project) {
        this.teamService = teamService;
        this.emailSender = emailSender;
        this.user = user;
        this.team = team;
        this.project = project;

        initContent();
        setSelectedItems();
    }

    private void setSelectedItems() {
        List<Position> positions = new ArrayList<>();
        positions.add(Position.DEVELOPER);
        if(team.getWaitForTeamLead() != null && !team.getWaitForTeamLead()) {
            positions.add(Position.TEAM_LEAD);
        }

        positionSelect.setItems(positions);
        positionSelect.setItemLabelGenerator(Position::getName);
    }

    private void initContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setSizeFull();

        emailField.setSizeFull();
        emailField.setLabel("Email");

        positionSelect.setWidthFull();
        positionSelect.setLabel("Должность");

        send.setText("Отправить");
        send.setWidthFull();
        send.addClickListener(event-> sendEmail());

        content.add(emailField, positionSelect, send);

        setWidth("400px");
        add(content);
    }

    private void sendEmail() {
        if(Arrays.stream(team.getWaitQueue()).anyMatch(email-> email.equals(emailField.getValue()))) {
            Utils.showErrorNotification("Приглашение этому пользователю уже отправлено");
            return;
        }
        Message message = new Message();
        message.setTo(emailField.getValue());
        message.setFrom("timetracksup@outlook.com");
        message.setPassword("75W-Syg-BQR-nzz");
        message.setSmtp("smtp-mail.outlook.com");
        message.setPort("587");
        message.setTheme("Регистрация в проект:" + project.getTitle());

        StringBuilder sb = new StringBuilder();
        sb.append("Здравствуйте!").append("\n");
        sb.append(user.getName()).append(" ").append(user.getSecondName()).append(" приглашает вас вступить в команду ").append(team.getName()).append("\n");
        sb.append("Присоединяйтесь к нам по ссылке ");
        VaadinServletRequest req = (VaadinServletRequest) VaadinService.getCurrentRequest();
        StringBuffer uriString = req.getRequestURL();
        if(positionSelect.getValue() == Position.TEAM_LEAD) {
            sb.append(uriString).append("register/").append("tm/").append(team.getId());
            team.setWaitForTeamLead(true);
        }
        if(positionSelect.getValue() == Position.DEVELOPER) {
            sb.append(uriString).append("register/").append("dv/").append(team.getId());
        }
        sb.append("\n").append("До встречи!");

        message.setBody(sb.toString());
        emailSender.sendEmail(message);
        String[] waitQUeue = new String[team.getWaitQueue().length + 1];
        System.arraycopy(team.getWaitQueue(), 0, waitQUeue, 0, team.getWaitQueue().length);
        waitQUeue[team.getWaitQueue().length] = emailField.getValue();
        team.setWaitQueue(waitQUeue);
        teamService.save(team);
        close();
    }
}
