package com.example.victor_pc.chatapplication.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import java.util.StringTokenizer;

import com.example.victor_pc.chatapplication.R;
import com.example.victor_pc.chatapplication.core.chat.ChatContract;
import com.example.victor_pc.chatapplication.core.chat.ChatPresenter;
import com.example.victor_pc.chatapplication.events.PushNotificationEvent;
import com.example.victor_pc.chatapplication.models.Chat;
import com.example.victor_pc.chatapplication.ui.adapters.ChatRecyclerAdapter;
import com.example.victor_pc.chatapplication.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;



public class ChatFragment extends Fragment implements ChatContract.View, TextView.OnEditorActionListener {
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    String message,s2,receiverFirebaseToken,receiver,receiverUid,sender,senderUid;

    private ProgressDialog mProgressDialog;

    private ChatRecyclerAdapter mChatRecyclerAdapter;

    private ChatPresenter mChatPresenter;

    public static ChatFragment newInstance(String receiver,
                                           String receiverUid,
                                           String firebaseToken) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_RECEIVER, receiver);
        args.putString(Constants.ARG_RECEIVER_UID, receiverUid);
        args.putString(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) view.findViewById(R.id.edit_text_message);




    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mETxtMessage.setOnEditorActionListener(this);

        mChatPresenter = new ChatPresenter(this);
        mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getArguments().getString(Constants.ARG_RECEIVER_UID));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            sendMessage();
            return true;
        }
        return false;
    }

    public void sendMessage() {
        message = mETxtMessage.getText().toString();
        receiver = getArguments().getString(Constants.ARG_RECEIVER);
        receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
        sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
      //  functionInappropriateWordFinder(message);
        functionConvertLowerCase(message);
       // Toast.makeText(getActivity().getApplicationContext(),message,Toast.LENGTH_SHORT).show();
       // mChatPresenter.sendMessage(getActivity().getApplicationContext(), chat, receiverFirebaseToken);
       //


    }
    public void functionInappropriateWordFinder(String s){
        Chat chat = new Chat(this.sender,
                this.receiver,
                this.senderUid,
                this. receiverUid,
                this.message,
                System.currentTimeMillis());
        int k=0;
        String badWords[]={"idiot","ass","stupid","fuck","bitch"};
        for(int i=0;i<badWords.length;i++){
            if((s.contains(badWords[i]))) {
                k++; //note this will remove spaces at the end
            }
        }
        if(k>=1)
            Toast.makeText(getContext(),"No. You can't use bad words",Toast.LENGTH_SHORT).show();
        else
            mChatPresenter.sendMessage(getActivity().getApplicationContext(),chat, this.receiverFirebaseToken);

    }

    public void functionConvertLowerCase(String s){
        s = s.toLowerCase();
       // Toast.makeText(getActivity().getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        functionPunctuationRemoval(s);
    }
    public void functionPunctuationRemoval(String s){
        s = s.replaceAll("[^a-zA-Z\\s]", "").replaceAll("\\s+", " ");
       // Toast.makeText(getActivity().getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        functionStopWordRemoval(s);
    }
    public void functionStopWordRemoval(String s){
        // String s="I love this phone, its super fast and there's so much new and cool things with jelly bean....but of recently I've seen some bugs.";
        String stopWords[]={"you","dont","this","a","a","is","he","she","able",	"about",	"across",	"after",	"all",	"almost",	"also",	"am",	"among",	"an",	"and",	"any",	"are",	"as",	"at",	"be",	"because",	"been",	"but",	"by",	"can",	"cannot",	"could",	"dear",	"did",	"do",	"does",	"either",	"else",	"ever",	"every",	"for",	"from",	"get",	"got",	"had",	"has",	"have",	"he",	"her",	"hers",	"him",	"his",	"how",	"however",	"i",	"if",	"in",	"into",	"is",	"it",	"its",	"just",	"least",	"let",	"like",	"likely",	"may",	"me",	"might",	"most",	"must",	"my",	"neither",	"no",	"nor",	"not",	"of",	"off",	"often",	"on",	"only",	"or",	"other",	"our",	"own",	"rather",	"said",	"say",	"says",	"she",	"should",	"since",	"so",	"some",	"than",	"that",	"the",	"their",	"them",	"then",	"there",	"these",	"they",	"this",	"tis",	"to",	"too",	"twas",	"us",	"wants",	"was",	"we",	"were",	"what",	"when",	"where",	"which",	"while",	"who",	"whom",	"why",	"will",	"with",	"would",	"yet",	"you",	"your","hai"};
        for(int i=0;i<stopWords.length;i++){
            if(s.contains(stopWords[i])){
                s=s.replaceAll(stopWords[i]+"\\s+", ""); //note this will remove spaces at the end
            }
        }
        System.out.println(s);
        Toast.makeText(getActivity().getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        functionTokenize(s);
    }
    public void functionTokenize(String s){
        StringTokenizer defaultTokenizer = new StringTokenizer(s);
        System.out.println("Total number of tokens found : " + defaultTokenizer.countTokens());
        while (defaultTokenizer.hasMoreTokens())
        {
//            System.out.println(defaultTokenizer.nextToken());
            //       s2= s2 + " " + defaultTokenizer.nextToken();
           Toast.makeText(getActivity().getApplicationContext(),defaultTokenizer.nextToken(),Toast.LENGTH_SHORT).show();

        }
        System.out.println("Total number of tokens found : " + defaultTokenizer.countTokens());
        functionInappropriateWordFinder(s);
    }


    @Override
    public void onSendMessageSuccess() {
        mETxtMessage.setText("");
       Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendMessageFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMessagesSuccess(Chat chat) {
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(new ArrayList<Chat>());
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
    }

    @Override
    public void onGetMessagesFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onPushNotificationEvent(PushNotificationEvent pushNotificationEvent) {
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
            mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    pushNotificationEvent.getUid());
        }
    }
}
