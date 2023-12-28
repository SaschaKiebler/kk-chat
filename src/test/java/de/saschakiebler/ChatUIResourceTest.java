

package de.saschakiebler;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
class ChatUIResourceTest {

//     @Mock
//     Template chat;

//     @Mock
//     ChatUIService chatService;

//     @Mock
//     Sse sse;

//     @Mock
//     SseEventSink eventSink;

//     @Mock
//     ConversationService conversationService;

//     @Mock
//     MessageService messageService;

//     @InjectMocks
//     ChatUIResource chatUIResource;

//     ConversationDTO conversationDTO;

//     MessageDTO messageDTO;



// @BeforeEach
// void setup() {
//     conversationService = mock(ConversationService.class);
//     messageService = mock(MessageService.class);
//     chatService = mock(ChatUIService.class);
//     chat = mock(Template.class);
//     sse = mock(Sse.class);
//     eventSink = mock(SseEventSink.class);
//     conversationDTO = new ConversationDTO();
//     messageDTO = new MessageDTO();
//     chatUIResource = new ChatUIResource(chat, chatService, conversationService, messageService);

// }

   
// @Test
// void testGetConversation() {
//     // given
//     Conversation conversation = new Conversation();
//     conversation.setId(1L);
//     conversation.setMessages(Collections.emptyList());
//     conversation.setTimestamp(LocalDateTime.of(2021, 1, 1, 1, 1, 1));
//     conversation.setName("topic");
//     when(conversationService.getConversation("1")).thenReturn(conversation);
    
    
//     // when
//     TemplateInstance templateInstance = chatUIResource.getConversation("1");

//     // then
//     verify(chat).data("conversation", conversation);
//     verify(chat).data("messages", conversation.getMessages());
//     verify(chat).data("name", conversation.getName());
//     verify(chat).data("conversationId", conversation.getId());
//     verify(chat).data("conversationIdString", conversation.getId().toString());
//     verify(chat).render();
//     assertEquals(chat, templateInstance);


// }

}