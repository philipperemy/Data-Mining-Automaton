/*WeightsContainer* weightsContainer = new WeightsContainer();

    double weights[Utils::MAX_WEIGHTS][Utils::MAX_WEIGHTS];

    for(int i=0; i<Utils::MAX_WEIGHTS; i++)
        for(int j=0; j<Utils::MAX_WEIGHTS; j++)
            weights[i][j] = i+j;

    cout << "last inserted value : " << weightsContainer->getLastInsertedId() << endl;

    weightsContainer->addWeights(weights);

    cout << "last inserted value : " << weightsContainer->getLastInsertedId() << endl;

    cout << "random inserted value : " << weightsContainer->getWeightsValue(1, 20, 21) << endl;

    //Regarder sur internet comment faire
    //En attendant on va recréer le tableau en faisant des get de double
    cout << "get Array Value : " << (weightsContainer->getWeights(1))[20][21] << endl;

    delete(weightsContainer);
    */