package de.saschakiebler.Service;

import de.saschakiebler.dto.MessageDTO;
import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Conversation;
import de.saschakiebler.repository.ConversationRepository;
import de.saschakiebler.service.ConversationService;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@QuarkusTest
class ConversationServiceTest {

    @Mock
    private Conversation conversation;


    @Mock
    private ConversationRepository conversationRepository;


    @InjectMocks
    private ConversationService conversationService;


    @Test
    void testGetChatMemory() {
        // given

        conversationRepository = mock(ConversationRepository.class);

        when(
            conversationRepository.getConversationById(1L)
            )
        .thenReturn(
            new Conversation(1L, "TestConversation", LocalDateTime.now())
            );
        conversationService = new ConversationService();


        LocalDateTime timestamp = LocalDateTime.now();
        List<MessageDTO> expectedMessageDTOs = new ArrayList<>();
      
        expectedMessageDTOs.add(new MessageDTO(1L,MessageRoles.USER.getRole(), "TestMessage", timestamp));
       


        List<MessageDTO> messageDTOs = conversationService.getChatMemory(1L);

        assertEquals(expectedMessageDTOs, messageDTOs);



    }
}