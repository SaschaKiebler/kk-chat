package de.saschakiebler.Service;

import de.saschakiebler.dto.MessageDTO;
import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Conversation;
import de.saschakiebler.model.Message;
import de.saschakiebler.service.ConversationService;
import io.quarkus.test.InjectMock;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.google.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ConversationServiceTest {

    @Mock
    private Conversation conversation;


    @InjectMock
    private ConversationService conversationService;


    @Test
    void testGetChatMemory() {
        // given
        when(
            conversation.findById(1L)
            )
        .thenReturn(
            List.of(new Conversation(1L, "TestConversation", LocalDateTime.now()))
            );
        conversationService = new ConversationService();


        LocalDateTime timestamp = LocalDateTime.now();
        List<MessageDTO> expectedMessageDTOs = new ArrayList<>();
      
        expectedMessageDTOs.add(new MessageDTO(1L,MessageRoles.USER.getRole(), "TestMessage", timestamp));
       


        List<MessageDTO> messageDTOs = conversationService.getChatMemory(1L);

        assertEquals(expectedMessageDTOs, messageDTOs);



    }
}