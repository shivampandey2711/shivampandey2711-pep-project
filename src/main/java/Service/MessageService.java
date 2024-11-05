package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO = new MessageDAO();

    public Message createMessage(Message message) {
        return messageDAO.createMessage(message);
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public List<Message> getMessagesByUserId(int userId) {
        return messageDAO.getMessagesByUserId(userId);
    }

    public Message updateMessage(int messageId, Message message) {
        message.setMessage_id(messageId);
        boolean isUpdated = messageDAO.updateMessage(message);
        return isUpdated ? messageDAO.getMessageById(messageId) : null;
    }

    public boolean deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> getAllMessagesForUser(int userId) {
        return messageDAO.getAllMessagesForUser(userId);
    }

}
