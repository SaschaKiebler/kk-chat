

package de.saschakiebler;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.saschakiebler.dto.ConversationDTO;
import de.saschakiebler.dto.MessageDTO;
import de.saschakiebler.model.Conversation;
import de.saschakiebler.resource.ChatUIResource;
import de.saschakiebler.service.ChatUIService;
import de.saschakiebler.service.ConversationService;
import de.saschakiebler.service.MessageService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
class ChatUIResourceTest {

    @Mock
    Template chat;

    @Mock
    ChatUIService chatService;

    @Mock
    Sse sse;

    @Mock
    SseEventSink eventSink;

    @Mock
    ConversationService conversationService;

    @Mock
    MessageService messageService;

    @InjectMocks
    ChatUIResource chatUIResource;

    ConversationDTO conversationDTO;

    MessageDTO messageDTO;



@BeforeEach
void setup() {
    conversationService = mock(ConversationService.class);
    messageService = mock(MessageService.class);
    chatService = mock(ChatUIService.class);
    chat = mock(Template.class);
    sse = mock(Sse.class);
    eventSink = mock(SseEventSink.class);
    conversationDTO = new ConversationDTO();
    messageDTO = new MessageDTO();
    chatUIResource = new ChatUIResource(chat, chatService, conversationService, messageService);

}

   
@Test
void testGetConversation() {
    // given
    Conversation conversation = new Conversation();
    conversation.setId(1L);
    conversation.setMessages(Collections.emptyList());
    conversation.setTimestamp(LocalDateTime.of(2021, 1, 1, 1, 1, 1));
    conversation.setName("topic");
    when(conversationService.getConversation("1")).thenReturn(conversation);
    
    
    // when
    TemplateInstance templateInstance = chatUIResource.getConversation("1");

    // then
    verify(chat).data("conversation", conversation);
    verify(chat).data("messages", conversation.getMessages());
    verify(chat).data("name", conversation.getName());
    verify(chat).data("conversationId", conversation.getId());
    verify(chat).data("conversationIdString", conversation.getId().toString());
    verify(chat).render();
    assertEquals(chat, templateInstance);


}

}