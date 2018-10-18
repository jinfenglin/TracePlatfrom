package computationEngine.Model.ModelUpdatePolicy;

/**
 *
 */
public enum UpdatePolicyType {
    DUMMY,
    CONSTANT, // Baseline for the model update policy
    CONDITIONAL, // Exam the model with a small set of data, if the result is lower than the threshold then rebuild/update the trace model
    NEW_DATA_DRIVEN // Collect the training data as the program runs, if enough training data is collected then trigger the model update
}
