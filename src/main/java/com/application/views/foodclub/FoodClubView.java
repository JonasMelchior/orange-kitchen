package com.application.views.foodclub;

import com.application.entity.kitchen.club.FoodClubEvent;
import com.application.service.foodclub.IFoodClubEventService;
import com.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PageTitle("Orange Food Club")
@Route(value = "food-club", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class FoodClubView extends VerticalLayout {

    @Autowired
    private IFoodClubEventService foodClubEventService;
    List<FoodClubEvent> foodClubEvents = new ArrayList<>();
    Grid<FoodClubEvent> foodClubEventGrid = new Grid<>(FoodClubEvent.class, false);

    Dialog addFoodClubEventDialog = new Dialog(createFoodClubEventFormDialogLayout());

    Upload upload;


    public FoodClubView() {

        Button openAddFoodClubEventDialogButton = new Button("Add new food club event", event -> {
            addFoodClubEventDialog.open();
        });



        foodClubEventGrid.addComponentColumn(event -> {
           DateTimePicker dateTimePicker = new DateTimePicker();
            String[] date = event.getMeetingDate().split("-");
            String[] time = event.getMeetingTime().split(":");
            LocalDateTime localDateTime = LocalDateTime.of(Integer.parseInt(date[0]),
                    Integer.parseInt(date[1]),
                    Integer.parseInt(date[2]),
                    Integer.parseInt(time[0]),
                    Integer.parseInt(time[1]),
                    Integer.parseInt(time[2]));
            dateTimePicker.setValue(localDateTime);
            dateTimePicker.addValueChangeListener( e -> {
                StringBuilder dateEdited = new StringBuilder();
                dateEdited.append(dateTimePicker.getValue().getYear()).append("-").append(dateTimePicker.getValue().getMonthValue()).append("-").append(dateTimePicker.getValue().getDayOfMonth());

                StringBuilder timeEdited = new StringBuilder();
                timeEdited.append(dateTimePicker.getValue().getHour()).append(":").append(dateTimePicker.getValue().getMinute()).append(":").append(dateTimePicker.getValue().getSecond());

                event.setMeetingDate(dateEdited.toString());
                event.setMeetingTime(timeEdited.toString());

                foodClubEventService.saveFoodClubEvent(event);
            });

            return dateTimePicker;
        }).setHeader("Meeting time and date");
        foodClubEventGrid.addComponentColumn(event -> {
            String participantsString = event.getParticipants();
            participantsString = participantsString.replace("[", "");
            participantsString = participantsString.replace("]", "");

            List<String> participants = new ArrayList<>(Arrays.stream(participantsString.split(",")).toList());
            participants.replaceAll(String::trim);

            MultiSelectListBox<String> multiSelectListBox = new MultiSelectListBox<>();
            multiSelectListBox.setItems(getPeopleFromKitchen());
            multiSelectListBox.select(participants);

            multiSelectListBox.addValueChangeListener( e -> {
                event.setParticipants(multiSelectListBox.getSelectedItems().toString());
                foodClubEventService.saveFoodClubEvent(event);
            });

            multiSelectListBox.setHeight("200px");
            return multiSelectListBox;
        }).setHeader("Participants");
        foodClubEventGrid.addComponentColumn(event -> {
            String chefsString = event.getChefs();
            chefsString = chefsString.replace("[", "");
            chefsString = chefsString.replace("]", "");

            List<String> chefs = new ArrayList<>(Arrays.stream(chefsString.split(",")).toList());
            chefs.replaceAll(String::trim);

            MultiSelectListBox<String> multiSelectListBox = new MultiSelectListBox<>();
            multiSelectListBox.setItems(getPeopleFromKitchen());
            multiSelectListBox.select(chefs);

            multiSelectListBox.addValueChangeListener( e -> {
                event.setChefs(multiSelectListBox.getSelectedItems().toString());
                foodClubEventService.saveFoodClubEvent(event);
            });
            multiSelectListBox.setHeight("200px");
            return multiSelectListBox;
        }).setHeader("Chefs");
        foodClubEventGrid.addComponentColumn(event -> {
            VerticalLayout imageContainer = new VerticalLayout();

            MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
            upload = new Upload(buffer);
            upload.setAcceptedFileTypes("image/jpeg","image/jpg", "image/png", "image/gif");

            upload.addSucceededListener(e -> {
                String attachmentName = e.getFileName();
                try {
                    // The image can be jpg png or gif, but we store it always as png file in this example
                    BufferedImage inputImage = ImageIO.read(buffer.getInputStream(attachmentName));
                    ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
                    ImageIO.write(inputImage, "png", pngContent);
                    saveFoodPicture(pngContent.toByteArray(), event);
                    showImage(event, imageContainer);
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            });
            showImage(event, imageContainer);
            VerticalLayout layout = new VerticalLayout(imageContainer, upload);
            layout.setDefaultHorizontalComponentAlignment(Alignment.BASELINE);
            return layout;
        }).setHeader("Menu");
        foodClubEventGrid.addComponentColumn(event -> {
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_ERROR,
                    ButtonVariant.LUMO_TERTIARY);

            deleteButton.addClickListener( e -> {
                foodClubEventService.deleteFoodClubEvent(event);
                foodClubEvents.remove(event);
                foodClubEventGrid.setItems(foodClubEvents);
            });

            return deleteButton;
        }).setHeader("Manage");

        VerticalLayout mainLayout = new VerticalLayout(foodClubEventGrid);
        mainLayout.setSizeFull();




        add(openAddFoodClubEventDialogButton, mainLayout);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSizeFull();

    }

    private void saveFoodPicture(byte[] imageBytes, FoodClubEvent foodClubEvent) {
        foodClubEvent.setFoodPicture(imageBytes);
        foodClubEventService.saveFoodClubEvent(foodClubEvent);
    }

    private void showImage(FoodClubEvent foodClubEvent, VerticalLayout imageContainer) {
        Image image = foodClubEventService.generateFoodImage(foodClubEvent);
        image.setWidth("200px");
        image.setHeight("200px");
        imageContainer.removeAll();
        imageContainer.add(image);
    }


    @PostConstruct
    public void postConstructor() {
        foodClubEvents = foodClubEventService.findAll();
        foodClubEventGrid.setItems(foodClubEvents);
    }

    private VerticalLayout createFoodClubEventFormDialogLayout() {
        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setStep(Duration.ofMinutes(30));

        MultiSelectListBox<String> participantsListBox = new MultiSelectListBox<>();
        participantsListBox.setItems(getPeopleFromKitchen());
        participantsListBox.getStyle().set("border", "2px solid black");


        MultiSelectListBox<String> chefsListBox = new MultiSelectListBox<>();
        chefsListBox.setItems(getPeopleFromKitchen());
        chefsListBox.getStyle().set("border", "2px solid black");

        Button button = new Button("Make food club event", event -> {
            StringBuilder date = new StringBuilder();
            date.append(dateTimePicker.getValue().getYear()).append("-").append(dateTimePicker.getValue().getMonthValue()).append("-").append(dateTimePicker.getValue().getDayOfMonth());

            StringBuilder time = new StringBuilder();
            time.append(dateTimePicker.getValue().getHour()).append(":").append(dateTimePicker.getValue().getMinute()).append(":").append(dateTimePicker.getValue().getSecond());

            FoodClubEvent foodClubEvent = new FoodClubEvent(
                    date.toString(),
                    time.toString(),
                    participantsListBox.getSelectedItems().toString(),
                    chefsListBox.getSelectedItems().toString()
            );

            foodClubEventService.saveFoodClubEvent(foodClubEvent);
            updateFoodClubEventGrid(foodClubEvent);
            addFoodClubEventDialog.close();
        });

        VerticalLayout verticalLayout = new VerticalLayout(
                dateTimePicker,
                new H3("Participants"),
                participantsListBox,
                new H3("Chefs"),
                chefsListBox,
                button
        );

        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return verticalLayout;
    }

    private void updateFoodClubEventGrid(FoodClubEvent foodClubEvent) {
        foodClubEvents.add(foodClubEvent);
        foodClubEventGrid.setItems(foodClubEvents);
    }

    private List<String> getPeopleFromKitchen() {
        List<String> retList = new ArrayList<>();

        retList.add("Bo Cui");
        retList.add("Agnes Hein");
        retList.add("Birgitta Nebel");
        retList.add("Christian N. M. Nielsen");
        retList.add("Christoffer Terp");
        retList.add("Daniel Kamperlund");
        retList.add("Gert Skau Vincentzen");
        retList.add("Gina Janoki");
        retList.add("Hannah Engmose");
        retList.add("Heini Poulsen");
        retList.add("Helene Lefmann");
        retList.add("Jonas (Kitchen Commando)");
        retList.add("Kasper Guttorm Andersen");
        retList.add("Long Ngo");
        retList.add("Marcus Galea Jacobsen");
        retList.add("Mette Elizabeth Kristensen");
        retList.add("Mikkel Kannik");
        retList.add("Ronja Toft");
        retList.add("Ronja Valentina Custodio");
        retList.add("William Carlsen");

        return retList;
    }

}
