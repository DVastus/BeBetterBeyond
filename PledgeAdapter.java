package com.firstapp.bbb;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PledgeAdapter extends RecyclerView.Adapter<PledgeAdapter.PledgeViewHolder> {

    private final List<Pledge> pledges;
    private final Context context;

    public PledgeAdapter(Context context, List<Pledge> pledges) {
        this.context = context;
        this.pledges = pledges;
    }

    @NonNull
    @Override
    public PledgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pledge, parent, false);
        return new PledgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PledgeViewHolder holder, int position) {
        Pledge p = pledges.get(position);

        holder.descriptionText.setText(p.getDescription());
        holder.conditionText.setText(p.getCondition());
        holder.dueDateText.setText("Due: " + (p.getDueDate() != null ? p.getDueDate() : "N/A"));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("pledgeId", p.getId());
            context.startActivity(intent);
        });

        if (p.getIsMutual()) {
            holder.mutualPledgeText.setVisibility(View.VISIBLE);
            holder.mutualPledgeText.setText("Mutual: " + p.getMutualDescription());
        } else {
            holder.mutualPledgeText.setVisibility(View.GONE);
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && p.getUserId() != null) {
            String currentUserId = currentUser.getUid();

            if (currentUserId.equals(p.getUserId())) {
                holder.agreeButton.setEnabled(false);
                holder.agreeButton.setText("Your pledge");
            } else if (p.getIsAccepted()) {
                if (currentUserId.equals(p.getAcceptedBy())) {
                    holder.agreeButton.setEnabled(false);
                    holder.agreeButton.setText("You accepted this pledge");
                } else {
                    holder.agreeButton.setEnabled(false);
                    holder.agreeButton.setText("Already accepted");
                }
            } else {
                holder.agreeButton.setEnabled(true);
                holder.agreeButton.setText("Agree to pledge");

                holder.agreeButton.setOnClickListener(v -> {
                    p.setAcceptedBy(currentUserId);
                    p.setIsAccepted(true);

                    FirebaseFirestore.getInstance().collection("pledges")
                            .document(p.getId())
                            .update("acceptedBy", currentUserId, "isAccepted", true)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(context, "Pledge accepted!", Toast.LENGTH_SHORT).show();
                                holder.agreeButton.setEnabled(false);
                                holder.agreeButton.setText("Pledge Accepted");
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Failed to accept pledge.", Toast.LENGTH_SHORT).show();
                            });
                });
            }

            boolean isCreator = currentUserId.equals(p.getUserId());
            boolean isAcceptor = currentUserId.equals(p.getAcceptedBy());

            boolean userCompleted;
            if (p.getIsMutual()) {
                userCompleted = (isCreator && p.getCreatorCompleted()) || (isAcceptor && p.getAcceptedCompleted());
            } else {
                userCompleted = p.getIsCompleted();
            }

            if ((isCreator || isAcceptor) && !userCompleted) {
                holder.completeButton.setVisibility(View.VISIBLE);
                holder.completeButton.setEnabled(true);
                holder.completeButton.setText("Mark as Completed");

                holder.completeButton.setOnClickListener(v -> {
                    if (p.getIsMutual()) {
                        String fieldToUpdate = isCreator ? "creatorCompleted" : "acceptorCompleted";

                        FirebaseFirestore.getInstance().collection("pledges")
                                .document(p.getId())
                                .update(fieldToUpdate, true)
                                .addOnSuccessListener(unused -> {
                                    if (isCreator) {
                                        p.setCreatorCompleted(true);
                                    } else {
                                        p.setAcceptorCompleted(true);
                                    }

                                    if (p.getCreatorCompleted() && p.getAcceptedCompleted()) {
                                        FirebaseFirestore.getInstance().collection("pledges")
                                                .document(p.getId())
                                                .update("isCompleted", true);
                                        p.setIsCompleted(true);
                                    }

                                    holder.completeButton.setEnabled(false);
                                    holder.completeButton.setText("Completed");
                                    Toast.makeText(context, "Marked as completed!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Failed to mark completed.", Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        FirebaseFirestore.getInstance().collection("pledges")
                                .document(p.getId())
                                .update("isCompleted", true)
                                .addOnSuccessListener(unused -> {
                                    p.setIsCompleted(true);
                                    holder.completeButton.setEnabled(false);
                                    holder.completeButton.setText("Completed");
                                    Toast.makeText(context, "Pledge marked as completed!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Failed to mark completed.", Toast.LENGTH_SHORT).show();
                                });
                    }
                });
            } else {
                holder.completeButton.setVisibility(View.VISIBLE);
                holder.completeButton.setEnabled(false);
                holder.completeButton.setText("Completed");
            }
        }
    }

    @Override
    public int getItemCount() {
        return pledges.size();
    }

    static class PledgeViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionText, conditionText, dueDateText, mutualPledgeText;
        Button agreeButton, completeButton;

        PledgeViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionText = itemView.findViewById(R.id.pledgeDescriptionText);
            conditionText = itemView.findViewById(R.id.pledgeConditionText);
            dueDateText = itemView.findViewById(R.id.pledgeDueDateText);
            mutualPledgeText = itemView.findViewById(R.id.mutualPledgeText);
            agreeButton = itemView.findViewById(R.id.agreePledgeButton);
            completeButton = itemView.findViewById(R.id.completeButton);
        }
    }
}
