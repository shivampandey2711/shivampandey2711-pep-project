package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController() {
        // Initialize services, which will in turn initialize DAOs
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * Sets up all API endpoints and returns a Javalin app.
     * @return a Javalin app object defining the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerUserHandler);
        app.post("/login", this::loginUserHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{id}", this::getMessageByIdHandler);
        app.delete("/messages/{id}", this::deleteMessageHandler);
        app.put("/messages/{id}", this::updateMessageHandler);
        app.get("/accounts/{userId}/messages", this::getAllMessagesFromUserHandler);

        return app;
    }

    /**
     * Handler for user registration.
     */
    private void registerUserHandler(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            
            // Check if username and password are provided
            if (account.getUsername() == null || account.getUsername().isEmpty() ||
                account.getPassword() == null || account.getPassword().length() < 4) {
                context.status(400);
                return;
            }
            
            // Attempt to register the user
            Account registeredAccount = accountService.registerUser(account);
            
            // If registration is successful, respond with 200 status and JSON body
            if (registeredAccount != null) {
                context.status(200).json(registeredAccount);
            } else {
                // If username already exists, respond with 400 status
                context.status(400);
            }
        } catch (Exception e) {
            context.status(400);
        }
    }
    /**
     * Handler for user login.
     */
    private void loginUserHandler(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            if (account.getUsername() == null || account.getPassword() == null) {
                context.status(400);
                return;
            }
            Account authenticatedAccount = accountService.loginUser(account.getUsername(), account.getPassword());
            if (authenticatedAccount != null) {
                context.status(200);
            } else {
                context.status(401);
            }
        } catch (Exception e) {
            context.status(500);
        }
    }

    /**
     * Handler for creating a new message.
     */
    private void createMessageHandler(Context context) {
        try {
            Message message = context.bodyAsClass(Message.class);
            if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
                context.status(400);
                return;
            }
            Message createdMessage = messageService.createMessage(message);
            context.status(200).json(createdMessage);
        } catch (Exception e) {
            context.status(400);
        }
    }

    /**
     * Handler for retrieving all messages.
     */
    private void getAllMessagesHandler(Context context) {
        try {
            List<Message> messages = messageService.getAllMessages();
            context.status(200);
        } catch (Exception e) {
            context.status(500);
        }
    }

    /**
     * Handler for retrieving a message by ID.
     */
    private void getMessageByIdHandler(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("id"));
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                context.status(200).json(message);
            } else {
                context.status(404).json("Message not found");
            }
        } catch (NumberFormatException e) {
            context.status(400).json("Invalid message ID");
        } catch (Exception e) {
            context.status(500).json("An error occurred while retrieving the message: " + e.getMessage());
        }
    }

    /**
     * Handler for deleting a message by ID.
     */
    private void deleteMessageHandler(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("id"));
            boolean isDeleted = messageService.deleteMessage(messageId);
            if (isDeleted) {
                context.status(200); // No Content
            }
        } catch (NumberFormatException e) {
            context.status(400);
        } catch (Exception e) {
            context.status(500);
        }
    }

    /**
     * Handler for updating a message by ID.
     */
    private void updateMessageHandler(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("id"));
            Message updatedMessageData = context.bodyAsClass(Message.class);
            if (updatedMessageData.getMessage_text() == null || 
                updatedMessageData.getMessage_text().isEmpty() || 
                updatedMessageData.getMessage_text().length() > 255) {
                context.status(200);
                return;
            }
    
            Message updatedMessage = messageService.updateMessage(messageId, updatedMessageData);
            if (updatedMessage != null) {
                context.status(200);
            } else {
                context.status(400);
            }
        } catch (NumberFormatException e) {
            context.status(400);
        } catch (Exception e) {
            context.status(500);
        }
    }

    private void getAllMessagesFromUserHandler(Context context) {
        try {
            int userId = Integer.parseInt(context.pathParam("userId"));
            List<Message> messages = messageService.getAllMessagesForUser(userId);
            if (!messages.isEmpty()) {
                context.status(200);
            } else {
                context.status(404);
            }
        } catch (NumberFormatException e) {
            context.status(400);
        } catch (Exception e) {
            context.status(500);
        }
    }


    
}
